package com.metro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metro.basic.BaseService;
import com.metro.basic.EntityDao;
import com.metro.dao.RuleMapper;
import com.metro.model.UserQuestion;
import com.metro.service.UserQuestionService;

@Service("userQuestionService")
public class UserQuestionServiceImpl extends BaseService<UserQuestion,String> implements UserQuestionService{
  
	
	@Autowired
	private RuleMapper ruleMapper;
	
	@Override
	protected EntityDao getEntityDao() {
		return ruleMapper;
	}
}