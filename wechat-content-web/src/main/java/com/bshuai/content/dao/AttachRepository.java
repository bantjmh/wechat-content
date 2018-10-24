package com.bshuai.content.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bshuai.content.entity.AttachEntity;

@Repository
public interface AttachRepository extends JpaRepository<AttachEntity, String>, JpaSpecificationExecutor<AttachEntity> {

	@Query(value = " from AttachEntity where businessid = ?1 order by type")
	List<AttachEntity> selectByBisinessid(String businessid);

	@Query(value = " from AttachEntity where businessid = ?1 and type = ?2")
	List<AttachEntity> selectByBusinessByType(String businessid, int type);

	@Query(value = " from AttachEntity where uid = ?1")
	List<AttachEntity> selectByUid(String uid);
}
