package com.loyi.cloud.stone.content.dao;

import com.loyi.cloud.stone.content.entity.ArticleSendRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleSendRecordRepository
		extends JpaRepository<ArticleSendRecordEntity, String>, JpaSpecificationExecutor<ArticleSendRecordEntity> {


}
