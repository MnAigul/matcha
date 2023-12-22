import React, {FC, useContext, useEffect, useState} from 'react';
import { BrowserRouter, Route, Navigate, useNavigate, Routes } from 'react-router-dom';
import LoginForm from "./components/login/LoginForm";
import Profile from './components/profile/Profile';
import {Context} from "./index";
import {observer} from "mobx-react-lite";
import { IUser } from './models/IUser';
import UserService from './services/UserService';
import CookieService from './services/CookieService';
import General from './components/general/General';
import { Container, Box } from '@mui/material';
import Header from './components/header/Header';
import AdminComponent from './components/admin/AdminComponent';
import User from './components/user/User';

const App: FC<{}> = () => {
  const {store} = useContext(Context);
  const [users, setUsers] = useState<IUser[]>([]);
  const navigate = useNavigate();



  useEffect(() => {
      if (CookieService.getAccessToken() || store.isAuth) {
          store.checkAuth();
          navigate(localStorage.getItem("storedPath") || "/general");
      }
  }, [])




  if (store.isLoading) {
    return <div>Загрузка...</div>
  }

  if (store.isAuth) {

    return (
    <div style={{ width: '100%', height: '100%' }}>
            <Header store={store} />
            <Routes>
              <Route path='/general' element={<General/>} />

                {CookieService.getRole()  === 'ROLE_ADMIN' ? (
                <Route path="/admin"  element={<AdminComponent />} />
              ) : (
                <Route path="/admin" element={<Navigate to='/general' />} />
              )}

              <Route path='/profile' element={<Profile/>} />

              <Route path='/user/:id' element={<User />} />
            </Routes>
    </div> 
    );
  }

  return (
    

          <Routes>
            <Route path='/' element={<LoginForm/>} />
            <Route path='/general' element={<Navigate to='/' />} />
            <Route path='/admin' element={<Navigate to='/' />} />
            <Route path='/profile' element={<Navigate to='/' />} />
            <Route path='/user/:id' element={<Navigate to='/' />} />

          </Routes>
  );

  

};

export default observer(App);
