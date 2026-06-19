package com.campus.collab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsResponse {
    
    private long totalUsers;
    private long totalPosts;
    private long totalComments;
    private long totalLikes;
}
