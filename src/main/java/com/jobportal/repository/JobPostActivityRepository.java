package com.jobportal.repository;

import com.jobportal.entity.IRecruiterJobs;
import com.jobportal.entity.JobPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface JobPostActivityRepository extends JpaRepository<JobPostActivity, Integer> {

    @Query(value = " SELECT COUNT(s.user_id) as totalCandidates,j.job_post_id,j.job_title,j.salary,j.job_type,j.posted_date,j.is_active,l.id as locationId,l.city,l.state,l.country,c.id as companyId,c.name FROM job_post_activity j " +
            " inner join job_location l " +
            " on j.job_location_id = l.id " +
            " INNER join job_company c  " +
            " on j.job_company_id = c.id " +
            " left join job_seeker_apply s " +
            " on s.job = j.job_post_id " +
            " where c.name = :company AND j.job_title IS NOT NULL AND TRIM(j.job_title) != '' " +
            " GROUP By j.job_post_id, j.job_title, j.salary, j.job_type, j.posted_date, j.is_active, l.id, l.city, l.state, l.country, c.id, c.name ORDER BY j.posted_date DESC" ,nativeQuery = true)
    List<IRecruiterJobs> getRecruiterJobs(@Param("company") String company);

    @Query(value = "SELECT * FROM job_post_activity j WHERE j.job_title IS NOT NULL AND TRIM(j.job_title) != '' ORDER BY j.posted_date DESC", nativeQuery = true)
    List<JobPostActivity> findAllValid();

    @Query(value = "SELECT * FROM job_post_activity j INNER JOIN job_location l on j.job_location_id=l.id  WHERE j" +
            ".job_title LIKE %:job%"
            + " AND j.job_title IS NOT NULL AND TRIM(j.job_title) != '' "
            + " AND (l.city LIKE %:location%"
            + " OR l.country LIKE %:location%"
            + " OR l.state LIKE %:location%) " +
            " AND (j.job_type IN(:type)) " +
            " AND (j.remote IN(:remote)) ORDER BY j.posted_date DESC", nativeQuery = true)
    List<JobPostActivity> searchWithoutDate(@Param("job") String job,
                                            @Param("location") String location,
                                            @Param("remote") List<String> remote,
                                            @Param("type") List<String> type);

    @Query(value = "SELECT * FROM job_post_activity j INNER JOIN job_location l on j.job_location_id=l.id  WHERE j" +
            ".job_title LIKE %:job%"
            + " AND j.job_title IS NOT NULL AND TRIM(j.job_title) != '' "
            + " AND (l.city LIKE %:location%"
            + " OR l.country LIKE %:location%"
            + " OR l.state LIKE %:location%) " +
            " AND (j.job_type IN(:type)) " +
            " AND (j.remote IN(:remote)) " +
            " AND (posted_date >= :date) ORDER BY j.posted_date DESC", nativeQuery = true)
    List<JobPostActivity> search(@Param("job") String job,
                                 @Param("location") String location,
                                 @Param("remote") List<String> remote,
                                 @Param("type") List<String> type,
                                 @Param("date") LocalDate searchDate);
}