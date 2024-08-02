import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, useNavigate, Routes } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from './redux/hooks'; // Import the custom hook
import { fetchCurrentUser, logout } from './features/authSlice';
import Navbar from './components/Common/Navbar.jsx';
import PrivateRoute from './components/PrivateRoute.jsx';
import HomePage from './pages/HomePage.jsx';
import Login from './components/Auth/Login.jsx';
import DashboardPage from './pages/DashboardPage.jsx';
import { Modal } from 'react-bootstrap';

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

  useEffect(() => {
    if(isLoginSuccess) {
      dispatch(fetchCurrentUser('admin')).catch(() => {
        setShowModal(true);
        setTimeout(() => {
          dispatch(logout());
          setShowModal(false);
          navigate('/login'); // Redirect to login page
        }, 3000);
      });
    }
  }, [dispatch, navigate, isLoginSuccess]);

  const handleLogout = async () => {
    dispatch(logout());
    navigate('/login'); // Redirect to login page
  };

  return (
    <>
      <Navbar onLogout={handleLogout} />
      <div className="container mt-4">
        <Routes>
          <Route exact path="/" element={<HomePage />} />
          <Route path="login" element={<Login />} />
          <Route path="dashboard" element={
            <PrivateRoute>
              <DashboardPage />
            </PrivateRoute>
          } />
          <Route path="*" element={<HomePage />} />
        </Routes>
      </div>
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Session Expired</Modal.Title>
        </Modal.Header>
        <Modal.Body>Your session has expired. You will be logged out.</Modal.Body>
      </Modal>
    </>
  );
};

export default App;
