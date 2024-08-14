import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, useNavigate, Routes } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from './redux/hooks'; // Import the custom hook
import { fetchCurrentUser, logout } from './features/authSlice';
import Navbar from './components/Common/Navbar.jsx';
import { Modal } from 'react-bootstrap';
import { useTheme } from './context/ThemeProvider';
import AppRoutes from './AppRoutes';
import './App.css';
import Layout from './components/Common/Layout.jsx';

const App = () => {
  return (
    <Router>
      <AppContent />
    </Router>
  );
};

const AppContent = () => {
  const dispatch = useAppDispatch();
  const isLoginSuccess = useAppSelector((state) => state.auth.isLoginSuccess);
  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(false);
  const { theme } = useTheme();

  useEffect(() => {
    dispatch(fetchCurrentUser());
  }, []);

  useEffect(() => {
    if (isLoginSuccess) {
      dispatch(fetchCurrentUser()).catch(() => {
        setShowModal(true);
        setTimeout(() => {
          dispatch(logout());
          setShowModal(false);
          navigate('/login'); 
        }, 3000);
      });
    }
  }, [navigate, dispatch, isLoginSuccess]);

  const handleLogout = async () => {
    console.log("LOGOUT calling..")
    dispatch(logout());
    navigate('/login'); // Redirect to login page
  };

  return (
    // <div className={`app ${theme === 'dark' ? 'bg-dark text-white' : 'bg-light text-dark'}`}>
    <div className={`secondary-bg text-color`}>
      <Layout handleLogout={handleLogout} />
    </div>
  );
};

export default App;
