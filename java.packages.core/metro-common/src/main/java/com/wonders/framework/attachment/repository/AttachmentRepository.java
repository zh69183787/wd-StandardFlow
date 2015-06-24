package com.wonders.framework.attachment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.attachment.entity.Attachment;
import com.wonders.framework.repository.MyRepository;

public interface AttachmentRepository extends MyRepository<Attachment, Long> {
	List<Attachment> findByGroup(String group);
	
	@Query(" from Attachment where  removed=0 and group=?1 and version = ?2")
	List<Attachment> findByGroupAndVersion(String group, String version);
	
	/**
	 * 根据 group统计
	 * @param group
	 * @return
	 */
	@Query("select count(*) from Attachment f where  f.removed=0 and f.group=?1 ")
	Long countAttachment(String group);
	
	/** 逻辑删除
	 * @param id
	 * @param value
	 * @return
	 */
	@Modifying
	@Query(" update Attachment e set e.removed=?2 where e.id=?1 ")
	public int unpdateRemovedById(Long id, Integer value);
	
	@Query(" from Attachment where removed=0 and group=?1 order by version desc, uploadTime desc")
	List<Attachment> findHistoryByGroup(String group);
	
}
