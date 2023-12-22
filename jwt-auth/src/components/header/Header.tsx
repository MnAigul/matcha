import React, { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import { observer } from 'mobx-react-lite';
import { AppBar, Toolbar, Typography, Button } from '@mui/material';
import Store from '../../store/store';
import './Header.css';
import CookieService from '../../services/CookieService';

interface HeaderProps {
  store: Store;
}

const Header: FC<HeaderProps> = ({ store }) => {
  const navigate = useNavigate();
  const handleLogout = async () => {
    try {
      await store.logout();
    } catch (error) {
      console.error('Error during logout:', error);
    }
  };

  const handleAdminClick = () => {

    if (CookieService.getRole() === "ROLE_USER") {
      navigate('/general');
    } else {
      navigate('/admin');
    }
  };

  const handleProfile = () => {
      navigate('/profile');

  };

  const handleGeneralClick = () => {
  
    navigate('/general');
  };

  return (
    <div className='header'>
      <div className='toolbar'>
        <h4><button onClick={handleGeneralClick}>Matcha</button></h4>
        <button onClick={handleLogout}>
          Logout
        </button>

        <button onClick={handleProfile}>
          Profile
        </button>

        <button onClick={handleAdminClick}>
          for Admin
        </button>
      </div>
    </div>
  );
};

export default observer(Header);