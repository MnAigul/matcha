import React, {FC, useContext, useState, useEffect } from 'react';
import { Context } from '../../index';
import {observer} from "mobx-react-lite";
import { UserFull } from '../../models/UserFull';
import UserService from '../../services/UserService';
import CookieService from '../../services/CookieService';
import { forEachChild } from 'typescript';
import { Link, useParams } from 'react-router-dom';

const User: FC = () => {
    const { id } = useParams<{ id: string }>();
    const [user, setUser] = useState<UserFull>();

  
    useEffect(() => {
        if (id != undefined)
            getUser(id);
        else
            console.error("Id is not defined");
      }, [id]);


  async function getUser(id: string) {
    try {
      const response = await UserService.fetchMyUser(id);
      const backendBirthDate = new Date(response.data.birth);
      backendBirthDate.setHours(14);
      const frontendBirthString = backendBirthDate.toISOString().split('T')[0];
      const updatedData = {
        ...response.data,
        birth: frontendBirthString,
      };
      setUser(updatedData);
    } catch (error) {
      console.error('Error fetching user:', error);
    }
  }


  return (
    <>
    <h1>Profile</h1>
    <div className='profileAndPhoto'>
        <div>
            {user ? (
              <>
                { (
                  <>
                    <p>Name: <span>{user.name}</span></p>
                    <p>Lastname: <span>{user.lastname}</span></p>
                    <p>City: <span>{user.city}</span></p>
                    <p>Birth: <span>{user?.birth}</span></p>
                    <p>Bio:<span>{user.bio}</span></p>
                  </>
                )}
              </>
            ) : (
              <p>Loading...</p>
            )}
        </div>
      
      
    </div>
    </>
  );

};

export default observer(User);