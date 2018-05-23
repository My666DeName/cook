package com.lin.service.impl;

import com.cook.dao.*;
import com.cook.entity.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lin.dao.ConsultDTOMapper;
import com.lin.dto.*;
import com.lin.service.ConsultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2018/5/18.
 */
@Service
public class ConsultServiceImpl1 implements ConsultService {
	
	@Autowired
	private ConsultDTOMapper consultDTOMapper;
	
	@Autowired
	private ImageTextMapper imageTextMapper;
	
	@Autowired
	private VideoMapper videoMapper;
	
	@Autowired
	private ConsultUserAdmireMapper consultUserAdmireMapper;
	
	@Autowired
	private CommentUserAdmireMapper commentUserAdmireMapper;
	
	@Autowired
	private ConsultMapper consultMapper;
	
	@Autowired
	private ConsultCommentMapper consultCommentMapper;
	
	//		分页设置
	final int pageSize = 10;
	
	public PageInfo commentList( String consultId, int pageNum ) {


//		获取ConsultComment列表，
// 		循环ConsultComment
// 		根据commentUserId获取对应的userId，
// 		通过userId去查询用户的userName、headImgUrl
//		最后将数据封装到对应的实体类中返回
		
		// 按照排序字段(数据表中对应的字段) + 倒序   进行排序
		String orderBy = "comment_date" + " desc";
		// pagehelper 分页失效：把分页语句加到查询语句前
		PageHelper.startPage( pageNum, pageSize, orderBy );
		List<ConsultCommentDTO> consultCommentDTOList = consultDTOMapper.listConsultComment( consultId );
		PageInfo<ConsultCommentDTO> pageInfo = new PageInfo<ConsultCommentDTO>( consultCommentDTOList );
		return pageInfo;
	}
	
	@Override
	public PageInfo consultList( int consultType, int pageNum ) {
		// 思路1：（同上的思路1：缺点：耗费大量服务器资源）
		
		// 思路2：两个表的联查不会影响数据库性能的同时还可以减轻服务器负担
		// 分析：资讯分两类：图文、视频
		// 从通过两个联表查询获取对应的图文或视频的数据
		// 添加到同一个list中
		// 将list交给分页插件处理，通过分页插件对数据进行处理
		
		// 创建一个总list
		List<Object> list = new ArrayList<Object>();
		
		// 按照排序字段(数据表中对应的字段) + 倒序   进行排序
		String orderBy = "publish_date" + " desc";
		
		// pagehelper 分页失效：把分页语句加到查询语句前
		PageHelper.startPage( pageNum, pageSize, orderBy );
		
		// 获取列表数据
		List<Consult> consultList = consultDTOMapper.consultList( consultType );
		
		// 对数据进行处理
		List<Object> objectList = new ArrayList<Object>();
		for( Consult consult : consultList ) {
			if( consult.getArticleType() == 0 ) {
				ConsultImageTextDTO consultImageTextDTO = new ConsultImageTextDTO();
				consultImageTextDTO.setConsultId( consult.getId() );
				consultImageTextDTO.setPublisherName( consult.getPublisherName() );
				consultImageTextDTO.setPublishDate( consult.getPublishDate() );
				// 通过 ArticleId 获取对应的数据
				ImageText imageText = imageTextMapper.selectByPrimaryKey( consult.getArticleId() );
				consultImageTextDTO.setTitle( imageText.getTitle() );
				consultImageTextDTO.setImageName( imageText.getImageName() );
				objectList.add( consultImageTextDTO );
			}
			if( consult.getArticleType() == 1 ) {
				ConsultVideoDTO consultVideoDTO = new ConsultVideoDTO();
				consultVideoDTO.setConsultId( consult.getId() );
				consultVideoDTO.setPublisherName( consult.getPublisherName() );
				consultVideoDTO.setPublishDate( consult.getPublishDate() );
				// 通过 ArticleId 获取对应的数据
				Video video = videoMapper.selectByPrimaryKey( consult.getArticleId() );
				consultVideoDTO.setTitle( video.getTitle() );
				consultVideoDTO.setVideoUrl( video.getVideoUrl() );
				consultVideoDTO.setPlayCount( video.getPlayCount() );
				objectList.add( consultVideoDTO );
			}
		}
		PageInfo<Object> pageInfo = new PageInfo<Object>( objectList );
		return pageInfo;
	}
	
	@Override
	public Integer consultAdmire( String consultId, String userId ) {
		int count = consultDTOMapper.selectConsultAdmire( consultId, userId );
		if( count == 0 ) {
			ConsultUserAdmire consultUserAdmire = new ConsultUserAdmire();
			consultUserAdmire.setId( UUID.randomUUID().toString() );
			consultUserAdmire.setConsultId( consultId );
			consultUserAdmire.setUserId( userId );
			Date date = new Date();
			consultUserAdmire.setAdmireDate( date.getTime() / 1000 );
			consultUserAdmireMapper.insertSelective( consultUserAdmire );
		} else {
			consultDTOMapper.delectConsultAdmire( consultId, userId );
		}
		return consultDTOMapper.countConsultAdmire();
	}
	
	@Override
	public Integer commentAdmire( String commentId, String userId ) {
		int count = consultDTOMapper.selectCommentAdmire( commentId, userId );
		if( count == 0 ) {
			CommentUserAdmire commentUserAdmire = new CommentUserAdmire();
			commentUserAdmire.setUserId( UUID.randomUUID().toString() );
			commentUserAdmire.setCommentId( commentId );
			commentUserAdmire.setUserId( userId );
			Date date = new Date();
			System.out.println( date.getTime() / 1000 );
			commentUserAdmire.setAdmireDate( date.getTime() / 1000 );
			commentUserAdmireMapper.insertSelective( commentUserAdmire );
		} else {
			consultDTOMapper.delectCommentAdmire( commentId, userId );
		}
		return consultDTOMapper.countCommentAdmire();
	}
	
	@Override
	public Consult getArticle( String consultId ) {
		Consult consult = consultMapper.selectByPrimaryKey( consultId );
		if( consult != null ) {
			if( consult.getArticleType() == 0 ) {
				ConsultImageTextDetailDTO consultImageTextDetailDTO = new ConsultImageTextDetailDTO();
				consultImageTextDetailDTO.setId( consultId );
				consultImageTextDetailDTO.setArticleId( consult.getArticleId() );
				consultImageTextDetailDTO.setPublisherName( consult.getPublisherName() );
				consultImageTextDetailDTO.setPublishDate( consult.getPublishDate() );
				// 通过 ArticleId 获取对应的数据
				ImageText imageText = imageTextMapper.selectByPrimaryKey( consult.getArticleId() );
				consultImageTextDetailDTO.setTitle( imageText.getTitle() );
				consultImageTextDetailDTO.setImageName( imageText.getImageName() );
				consultImageTextDetailDTO.setContent( imageText.getContent() );
				consultImageTextDetailDTO.setAdmireCount( imageText.getAdmireCount() );
				// 判断是否已经点赞
				int count = consultDTOMapper.selectConsultAdmire( consultId, consult.getUserId() );
				consultImageTextDetailDTO.setIsAdmired( count );
				return consultImageTextDetailDTO;
			}
			if( consult.getArticleType() == 1 ) {
				ConsultVideoDetailDTO consultVideoDetailDTO = new ConsultVideoDetailDTO();
				consultVideoDetailDTO.setId( consultId );
				consultVideoDetailDTO.setArticleId( consult.getArticleId() );
				consultVideoDetailDTO.setPublisherName( consult.getPublisherName() );
				consultVideoDetailDTO.setPublishDate( consult.getPublishDate() );
				// 通过 ArticleId 获取对应的数据
				Video video = videoMapper.selectByPrimaryKey( consult.getArticleId() );
				consultVideoDetailDTO.setTitle( video.getTitle() );
				consultVideoDetailDTO.setPlayCount( video.getPlayCount() );
				consultVideoDetailDTO.setAdmireCount( video.getAdmireCount() );
				consultVideoDetailDTO.setVideoUrl( video.getVideoUrl() );
				// 判断是否已经点赞
				int count = consultDTOMapper.selectConsultAdmire( consultId, consult.getUserId() );
				consultVideoDetailDTO.setIsAdmired( count );
				return consultVideoDetailDTO;
			}
		}
		return null;
	}
	
	@Override
	public String addComment( String consultId, String commentUserId, String content ) {
		// 查询是否存在对应的数据
		// 如果有，那就提示已存在信息
		// 如果没有并插入后返回数据为1，提示插入成功，否则返回失败
		
		int num = consultDTOMapper.existContextOfComment( consultId, commentUserId, content );
		if( num == 1 )
			return "已评论过";
		ConsultComment consultComment = new ConsultComment();
		consultComment.setId( UUID.randomUUID().toString() );
		consultComment.setContent( content );
		consultComment.setAdmireCount( 0 );
		consultComment.setCommentUserId( commentUserId );
		consultComment.setConsultId( consultId );
		consultComment.setCommentDate( new Date().getTime() / 1000 );
		int result = consultCommentMapper.insertSelective( consultComment );
		if( result == 1 )
			return "评论成功";
		return "评论失败";
	}
}
