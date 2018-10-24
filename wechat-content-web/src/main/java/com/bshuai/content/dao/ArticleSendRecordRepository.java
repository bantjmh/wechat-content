package com.bshuai.content.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.bshuai.content.entity.ArticleSendRecordEntity;

@Repository
public interface ArticleSendRecordRepository
		extends JpaRepository<ArticleSendRecordEntity, String>, JpaSpecificationExecutor<ArticleSendRecordEntity> {


}
