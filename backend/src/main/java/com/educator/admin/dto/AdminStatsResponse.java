package com.educator.admin.dto;

public class AdminStatsResponse {

    private long totalUsers;
    private long totalStudents;
    private long totalInstructors;
    private long totalAdmins;
    private long totalCourses;
    private long totalPublishedExams;
    private long activeEnrollments;
    private long totalCompletions;
    private long totalCertificates;

    public AdminStatsResponse(
            long totalUsers,
            long totalStudents,
            long totalInstructors,
            long totalAdmins,
            long totalCourses,
            long totalPublishedExams,
            long activeEnrollments,
            long totalCompletions,
            long totalCertificates
    ) {
        this.totalUsers = totalUsers;
        this.totalStudents = totalStudents;
        this.totalInstructors = totalInstructors;
        this.totalAdmins = totalAdmins;
        this.totalCourses = totalCourses;
        this.totalPublishedExams = totalPublishedExams;
        this.activeEnrollments = activeEnrollments;
        this.totalCompletions = totalCompletions;
        this.totalCertificates = totalCertificates;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public long getTotalInstructors() {
        return totalInstructors;
    }

    public long getTotalAdmins() {
        return totalAdmins;
    }

    public long getTotalCourses() {
        return totalCourses;
    }

    public long getTotalPublishedExams() {
        return totalPublishedExams;
    }

    public long getActiveEnrollments() {
        return activeEnrollments;
    }

    public long getTotalCompletions() {
        return totalCompletions;
    }

    public long getTotalCertificates() {
        return totalCertificates;
    }
}
