import React, { useState } from 'react';
import axios from 'axios';
import './loginform.css';

const LoginForm = ({ navigation }) => {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const [stayLoggedIn, setStayLoggedIn] = useState(true);
  const [ipSecurity, setIpSecurity] = useState(true);

  const handleLogin = async (e) => {
    e.preventDefault();

    const loginData = {
      id,
      password
    };

    try {
      const response = await axios.post('http://localhost:8080/auth/login', loginData, {
        headers: {
          'Content-Type': 'application/json',
        },
      });

      // 서버에서 받은 응답 처리
      console.log('로그인 성공:', response.data);
      localStorage.setItem('Authorization', response.data);
      navigation.navigate('Home');
    } catch (error) {
      console.error('로그인 오류:', error.response ? error.response.data : error.message);
      alert("아이디나 비밀번호를 확인해주세요.");
    }
  };

  return (
    <div className="login-container">
      <h1 className="naver-logo">Second Serve</h1>
      <form onSubmit={handleLogin} className="login-form">
        <div className="input-group">
          <label htmlFor="id">아이디 또는 전화번호</label>
          <input
            type="text"
            id="id"
            value={id}
            onChange={(e) => setId(e.target.value)}
            placeholder="아이디 또는 전화번호"
            required
          />
        </div>
        <div className="input-group">
          <label htmlFor="password">비밀번호</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="비밀번호"
            required
          />
        </div>
        <button type="submit" className="login-button">
          로그인
        </button>
      </form>
      <div className="bottom-links">
        <a href="/">비밀번호 찾기</a>
        <a href="/">아이디 찾기</a>
        <a href="/">회원가입</a>
      </div>
      <footer>
        <p>Copyright © Second Serve. All Rights Reserved.</p>
      </footer>
    </div>
  );
};

export default LoginForm;
