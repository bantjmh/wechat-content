package com.loyi.cloud.stone.content.dao;

import java.util.List;

import com.loyi.cloud.stone.content.entity.ArticleGroupRelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleGroupRelRepository
		extends JpaRepository<ArticleGroupRelEntity, String>, JpaSpecificationExecutor<ArticleGroupRelEntity> {

	@Query(" from ArticleGroupRelEntity where articleId = ?1")
	List<ArticleGroupRelEntity> select(String articleId);

	@Modifying
	@Query("delete from ArticleGroupRelEntity where articleId = ?1")
	void deleteByArticleId(String articleId);

	@Query(" from ArticleGroupRelEntity where groupId = ?1")
	List<ArticleGroupRelEntity> selectByGroupId(String groupId);

}
