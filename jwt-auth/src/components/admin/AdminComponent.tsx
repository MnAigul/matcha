import React, {FC, useContext, useState, useEffect } from 'react';
import CookieService from '../../services/CookieService';

const AdminComponent: FC = () => {

  useEffect(() => {
    localStorage.setItem('storedPath', '/admin');
  }, []);

    
    return (
        <div>
          <h1>Admin page</h1>
          <h2>Role: {CookieService.getRole()}</h2>
        </div>
    );
}

export default AdminComponent;