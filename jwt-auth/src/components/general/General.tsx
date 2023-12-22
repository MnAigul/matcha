import React, {FC, useContext, useState, useEffect } from 'react';
import UserService from '../../services/UserService';
import { UserGeneral } from '../../models/UserGeneral';
import './General.css';
import CookieService from '../../services/CookieService';
import { Link } from 'react-router-dom';


const General: FC = () => {
  const [users, setUsers] = useState<UserGeneral[]>([]);
  const [selectedRole, setSelectedRole] = useState('');

  useEffect(() => {
    localStorage.setItem('storedPath', '/general');
  }, []);


  async function getUsers() {
    try {
      const response = await UserService.fetchUsers();
      setUsers(response.data);
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  }

  async function handleRoleChange(id: string, role: string) {
    try {
      const response = await UserService.updateRole(id, role);
      console.log(response.data);
    } catch (error) {
      console.error("Error updating role of user");
    }

  }

  useEffect( () => {
    getUsers();
  }, [])

  return (
    <div>
      <h1>Главная страница</h1>
        <>
          <div className='cards'>
          {users && users.map((user) => (
            CookieService.getId() != user.id &&  (
              <Link to={`/user/${user.id}`} key={user.id}>
                  <div className='card'  key={user.id}>
                    <p>Name: {user.name}</p>
                    <p>Lastname: {user.lastname}</p>
                    <p>Email: {user.email}</p>
                    <p>Bio: {user.bio}</p>
                    <p>City: {user.city}</p>
                    <p>Birth: {new Date(new Date(user.birth).setHours(14)).toISOString().split('T')[0]}</p>
                    {CookieService.getRole() === 'ROLE_ADMIN' && (
                        <div>
                        <select
                          onChange={(e) => handleRoleChange(user.id, e.target.value)}>
                          <option value={(user.role == 'ROLE_USER') ? 'USER' : 'ADMIN'}>{user.role.substring(5)}</option>
                          <option value={(user.role == 'ROLE_USER') ? 'ADMIN' : 'USER'}>{(user.role == 'ROLE_USER') ? 'ADMIN' : 'USER'}</option>
                        </select>
                      </div>
                    )}
                  </div>
              </Link>
                
          
            ) 
          ))}
          </div>
        </>
    </div>
  );
};

export default General;