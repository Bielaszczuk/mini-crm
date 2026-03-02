package com.cbielaszczuk.crm.repository;
import com.cbielaszczuk.crm.model.ProjectModel;
import com.cbielaszczuk.crm.model.ProjectStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ProjectRepository extends JpaRepository<ProjectModel, Long> {
    @Query("SELECT p FROM ProjectModel p WHERE p.deletedAt IS NULL")
    List<ProjectModel> findAllActive();
    List<ProjectModel> findByStatus(ProjectStatusEnum status);
    @Query("SELECT p FROM ProjectModel p WHERE p.client.id = :clientId AND p.deletedAt IS NULL")
    List<ProjectModel> findByClientId(Long clientId);
    @Query("SELECT p FROM ProjectModel p WHERE p.client.id = :clientId")
    List<ProjectModel> findAllByClientId(Long clientId);
    List<ProjectModel> findByTitleContainingIgnoreCase(String title);
}
