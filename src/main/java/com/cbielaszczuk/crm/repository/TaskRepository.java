package com.cbielaszczuk.crm.repository;
import com.cbielaszczuk.crm.model.TaskModel;
import com.cbielaszczuk.crm.model.TaskStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long> {
    @Query("SELECT t FROM TaskModel t WHERE t.deletedAt IS NULL")
    List<TaskModel> findAllActive();

    @Query("SELECT t FROM TaskModel t WHERE t.project.client.user.id = :userId AND t.deletedAt IS NULL")
    List<TaskModel> findAllActiveByUserId(Long userId);

    @Query("SELECT t FROM TaskModel t WHERE t.id = :id AND t.project.client.user.id = :userId AND t.deletedAt IS NULL")
    Optional<TaskModel> findByIdAndUserId(Long id, Long userId);

    List<TaskModel> findByStatus(TaskStatusEnum status);
    @Query("SELECT t FROM TaskModel t WHERE t.project.id = :projectId AND t.deletedAt IS NULL")
    List<TaskModel> findByProjectId(Long projectId);
    @Query("SELECT t FROM TaskModel t WHERE t.project.id = :projectId")
    List<TaskModel> findAllByProjectId(Long projectId);
    List<TaskModel> findByTitleContainingIgnoreCase(String title);
}
