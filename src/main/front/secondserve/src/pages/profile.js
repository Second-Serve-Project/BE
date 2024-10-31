import React from "react";
import styled from "styled-components";

const ProfileContainer = styled.div`
  max-width: 935px;
  margin: 0 auto;
  padding: 20px;
`;

const Header = styled.div`
  display: flex;
  align-items: center;
  padding: 20px 0;
`;

const ProfileImage = styled.img`
  width: 150px;
  height: 150px;
  border-radius: 50%;
  margin-right: 50px;
`;

const ProfileInfo = styled.div`
  display: flex;
  flex-direction: column;
`;

const Username = styled.h2`
  font-size: 28px;
  margin-bottom: 10px;
`;

const Stats = styled.div`
  display: flex;
  gap: 20px;
  font-size: 16px;
  margin-bottom: 20px;
`;

const Stat = styled.span`
  font-weight: bold;
`;

const Bio = styled.p`
  font-size: 14px;
`;

const PostsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  padding-top: 30px;
`;

const PostImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const Profile = () => {
  return (
    <ProfileContainer>
      <Header>
        <ProfileImage src="https://via.placeholder.com/150" alt="profile" />
        <ProfileInfo>
          <Username>username</Username>
          <Stats>
            <Stat>10 posts</Stat>
            <Stat>200 followers</Stat>
            <Stat>180 following</Stat>
          </Stats>
          <Bio>This is a sample bio. Welcome to my profile!</Bio>
        </ProfileInfo>
      </Header>

      <PostsGrid>
        {/* 포스트 이미지들 */}
        <PostImage src="https://via.placeholder.com/300" alt="post1" />
        <PostImage src="https://via.placeholder.com/300" alt="post2" />
        <PostImage src="https://via.placeholder.com/300" alt="post3" />
        <PostImage src="https://via.placeholder.com/300" alt="post4" />
        <PostImage src="https://via.placeholder.com/300" alt="post5" />
        <PostImage src="https://via.placeholder.com/300" alt="post6" />
      </PostsGrid>
    </ProfileContainer>
  );
};

export default Profile;
