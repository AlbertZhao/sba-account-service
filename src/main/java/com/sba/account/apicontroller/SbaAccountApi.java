package com.sba.account.apicontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sba.account.mapper.UserMapper;
import com.sba.account.model.User;
import com.sba.account.rspmodel.RspModel;
import com.sba.account.utils.EncrytedPasswordUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@RestController
@RequestMapping("/api/v1")
@Api(description = "SBA Account Interface")
public class SbaAccountApi {

	@Autowired
	private UserMapper usermapper;

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ApiOperation(value = "SBA Account Register")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "ok"), @ApiResponse(code = 202, message = "Account Exist"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "No Authroization"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Error") })
	public ResponseEntity<RspModel> addUser(@ApiParam(name = "body", required = true) @RequestBody User user) {

		try {
			Integer checkaccount = usermapper.checkUser(user.getUsername());
			if (checkaccount != null) {
				RspModel rsp = new RspModel();
				rsp.setCode(202);
				rsp.setMessage("Account Exist");
				return new ResponseEntity<RspModel>(rsp, HttpStatus.OK);
			} else {
				User newuser = new User();
				String encrytedpassword = EncrytedPasswordUtils.encrytePassword(user.getPassword());
				newuser.setPassword(encrytedpassword);
				newuser.setName(user.getName());
				newuser.setUsername(user.getUsername());
				newuser.setRole(user.getRole());
				if (user.getRole().equals("user")) {
					newuser.setStatus(true);
				}else {
					newuser.setStatus(false);
				}
				usermapper.addUser(newuser);

				RspModel rsp = new RspModel();
				rsp.setCode(200);
				rsp.setMessage("Account Created");
				return new ResponseEntity<RspModel>(rsp, HttpStatus.CREATED);
			}

		} catch (Exception ex) {
			RspModel rsp = new RspModel();
			rsp.setCode(500);
			rsp.setMessage(ex.getMessage());
			return new ResponseEntity<RspModel>(rsp, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = "/query", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "SBA Find Account")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "ok"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "No Authroization"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Error") })
	public ResponseEntity<RspModel> queryUser(@ApiParam(name = "username", required = true) @RequestParam String username) {

		try {
			User user = usermapper.findUser(username);
			if (user != null) {
				RspModel rsp = new RspModel();
				rsp.setCode(200);
				rsp.setMessage("Ok");
				rsp.setData(user);
				return new ResponseEntity<RspModel>(rsp, HttpStatus.OK);	
				
			} else {
				RspModel rsp = new RspModel();
				rsp.setCode(404);
				rsp.setMessage("Account No Found");
				return new ResponseEntity<RspModel>(rsp, HttpStatus.NOT_FOUND);		
			}
			

		} catch (Exception ex) {
			RspModel rsp = new RspModel();
			rsp.setCode(500);
			rsp.setMessage(ex.getMessage());
			return new ResponseEntity<RspModel>(rsp, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
