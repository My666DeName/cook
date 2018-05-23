package com.lin.controller;

import com.cook.response.ApiResponse;
import com.github.pagehelper.PageInfo;
import com.lin.service.impl.ConsultServiceImpl1;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2018/5/18.
 */
@RestController
@RequestMapping("consult")
@Api(value = "consult", description = "资讯评论相关")
public class ConsultController {
	
	@Autowired
	private ConsultServiceImpl1 consultService;
	
	//	评论列表（分页）
//	 新增：  添加一个是否已经对该评论进行了点赞的提示
//	 待解决：从seesion中获取用户信息，列表中要判断用户是否已经点赞过
	@ApiOperation(value = "获取某一资讯内的全部评论", notes = "待处理：点赞问题")
	@GetMapping(value = "commentList/{consultId}/{pageNum}")
	public ApiResponse commentList( @ApiParam(name = "consultId", value = "资讯id", required = true) @PathVariable String consultId,
									@ApiParam(name = "pageNum", value = "页码", required = true) @PathVariable int pageNum ) {
		PageInfo pageInfo = consultService.commentList( consultId, pageNum );
		return IsNullOfData( pageInfo );
	}
	
	//	资讯列表按类型划分（分页）
	@ApiOperation(value = "获取资讯全部列表", notes = "0：饮食相关，1：日常生活")
	@PostMapping(value = "consultList")
	public ApiResponse consultList( @ApiParam(name = "consultType", value = "所属类型", required = true) @RequestParam(value = "consultType", defaultValue = "0") int consultType,
									@ApiParam(name = "pageNum", value = "页码", required = true) @RequestParam("pageNum") int pageNum ) {
		PageInfo pageInfo = consultService.consultList( consultType, pageNum );
		return IsNullOfData( pageInfo );
	}
	
	// 资讯点赞
	// 	返回：是否已经点赞   点赞总数
	@ApiOperation(value = "给某资讯点赞", notes = "返回值：0：取消点赞，1：添加点赞")
	@PostMapping(value = "consultAdmire")
	public ApiResponse consultAdmire( @ApiParam(name = "consultId", value = "资讯id", required = true) @RequestParam(value = "consultId") String consultId,
									  @ApiParam(name = "userId", value = "点赞用户id", required = true) @RequestParam("userId") String userId ) {
		return ApiResponse.ofSuccess( consultService.consultAdmire( consultId, userId ) );
	}
	
	// 评论点赞
	// 	返回：是否已经点赞   点赞总数
	@ApiOperation(value = "给某评论点赞", notes = "返回值：0：取消点赞，1：添加点赞")
	@PostMapping(value = "commentAdmire")
	public ApiResponse commentAdmire( @ApiParam(name = "commentId", value = "评论id", required = true) @RequestParam(value = "commentId") String commentId,
									  @ApiParam(name = "userId", value = "点赞用户id", required = true) @RequestParam("userId") String userId ) {
		return ApiResponse.ofSuccess( consultService.commentAdmire( commentId, userId ) );
	}
	
	// 资讯详情
	@ApiOperation(value = "通过资讯id获取资讯详情")
	@GetMapping(value = "getArticle/{consultId}")
	public ApiResponse getArticle( @ApiParam(name = "consultId", value = "资讯id", required = true) @PathVariable String consultId ) {
		return ApiResponse.ofSuccess( consultService.getArticle( consultId ) );
	}
	
	// 添加评论
	@ApiOperation(value = "评论某一资讯")
	@PostMapping(value = "addComment")
	public ApiResponse addComment( @ApiParam(name = "consultId", value = "资讯id", required = true) @RequestParam(value = "consultId") String consultId,
								   @ApiParam(name = "commentUserId", value = "评论者用户id", required = true) @RequestParam("commentUserId") String commentUserId,
								   @ApiParam(name = "content", value = "评论内容", required = true) @RequestParam("content") String content ) {
		return ApiResponse.ofSuccess( consultService.addComment( consultId, commentUserId, content ) );
	}
	
	private ApiResponse IsNullOfData( PageInfo pageInfo ) {
		if( pageInfo.getSize() == 0 ) {
			ApiResponse apiResponse = new ApiResponse();
			apiResponse.setCode( 200 );
			apiResponse.setMessage( "没有查询结果" );
			return apiResponse;
		}
		return ApiResponse.ofSuccess( pageInfo );
	}
}