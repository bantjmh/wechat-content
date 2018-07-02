package com.loyi.cloud.wechat.platform.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.loyi.cloud.wechat.platform.entity.ArticleSendRecordEntity;

@Repository
public interface ArticleSendRecordRepository
		extends JpaRepository<ArticleSendRecordEntity, String>, JpaSpecificationExecutor<ArticleSendRecordEntity> {


}
