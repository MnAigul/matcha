import './Profile.css';
import React, {FC, useContext, useState, useEffect } from 'react';
import { Context } from '../../index';
import {observer} from "mobx-react-lite";
import { UserFull } from '../../models/UserFull';
import UserService from '../../services/UserService';
import CookieService from '../../services/CookieService';
import { forEachChild } from 'typescript';

const Profile: FC = () => {
    const [file, setFile] = useState<File | null>(null);
    const [user, setUser] = useState<UserFull>();
    const [editing, setEditing] = useState(false);
    const [profileImage, setProfileImage] = useState<string | null>(null);
    const [editedData, setEditedData] = useState<Partial<UserFull>>({
        name: '',
        lastname: '',
        city: '',
        birth: '',
        bio: '',
        photo: '',
    });


    useEffect(() => {
      localStorage.setItem('storedPath', '/profile');
    }, []);
  
    useEffect(() => {
        getUser();
        
      }, []);


  async function getUser() {
    try {
      const response = await UserService.fetchMyUser(CookieService.getId());
      const backendBirthDate = new Date(response.data.birth);
      backendBirthDate.setHours(14);
      console.log("backendBirthDate = ", backendBirthDate);
      const frontendBirthString = backendBirthDate.toISOString().split('T')[0]; 
      console.log("frontendBirthString = ", frontendBirthString);
      const updatedData = {
        ...response.data,
        birth: frontendBirthString,
      };
      setUser(updatedData);
    
      console.log("GETUSER = ", response.data);
      const responsePhoto = await UserService.getPhoto(CookieService.getId());
      console.log(responsePhoto);
      console.log("PROFILEIMAGE = " + responsePhoto.blob);
      const imageUrl = URL.createObjectURL(new Blob([responsePhoto.data]));
//       const bytes = new Uint8Array(responsePhoto.data);
// const charArray = Array.from(bytes); // Преобразование Uint8Array в массив чисел
// const imageUrl = `data:image/jpeg;base64,${btoa(String.fromCharCode.apply(null, charArray))}`;
setProfileImage(imageUrl);
      setProfileImage(imageUrl);
    } catch (error) {
      console.error('Error fetching user:', error);
    }
  }

  

  
  const handleEditClick = () => {
    setEditing(true);
    setEditedData({
      name: user?.name || '',
      lastname: user?.lastname || '',
      city: user?.city || '',
      birth: user?.birth,
      bio: user?.bio || '',
    });
  };

  const handleSaveClick = async () => {
    try {
        console.log(editedData);
      const response = await UserService.updateUser(CookieService.getId(), editedData);
      const backendBirthDate = new Date(response.data.birth);
      backendBirthDate.setHours(14);
      console.log("backendBirthDate = ", backendBirthDate);
      const frontendBirthString = backendBirthDate.toISOString().split('T')[0]; 
      console.log("frontendBirthString = ", frontendBirthString);
      const updatedData = {
        ...response.data,
        birth: frontendBirthString,
      };
      setUser(updatedData);
      setEditing(false);
    } catch (error) {
      console.error('Error updating user:', error);
    }
  };

  const handleCancelClick = () => {
    setEditing(false);
    setEditedData({
      name: user?.name || '',
      lastname: user?.lastname || '',
      city: user?.city || '',
      birth: user?.birth,
      bio: user?.bio || '',
    });
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEditedData({ ...editedData, [e.target.name]: e.target.value });
  };

  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    setFile(e.target.files?.[0] || null);
  };

  const handleUpload = async () => {
    if (file) {
      const formData = new FormData();
      formData.append('file', file);
      console.log("FORMDATA = " + formData);
      const response = await UserService.uploadFile(CookieService.getId(), formData);
      console.log(response);
      const imageUrl = URL.createObjectURL(file);
      setProfileImage(imageUrl);
    }

  }

  return (
    <>
    <h1>Profile</h1>
    <div className='profileAndPhoto'>
        <div>
            {user ? (
              <>
                {editing ? (
                  <>
                    <p>Name: <input type="text" name="name" value={editedData.name} onChange={handleInputChange} /></p>
                    <p>Lastname: <input type="text" name="lastname" value={editedData.lastname} onChange={handleInputChange} /></p>
                    <p>City: <input type="text" name="city" value={editedData.city} onChange={handleInputChange} /></p>
                    <p>Birth: <input type="date" name="birth" value={editedData?.birth} onChange={handleInputChange} /></p>
                    <p>Bio: <input type="text" name="bio" value={editedData.bio} onChange={handleInputChange} /></p>
                    <button onClick={handleSaveClick}>Save</button>
                    <button onClick={handleCancelClick}>Cancel</button>
                  </>
                ) : (
                  <>
                    <p>Name: <span>{user.name}</span></p>
                    <p>Lastname: <span>{user.lastname}</span></p>
                    <p>City: <span>{user.city}</span></p>
                    <p>Birth: <span>{user?.birth}</span></p>
                    <p>Bio:<span>{user.bio}</span></p>
                    <button onClick={handleEditClick}>Edit</button>
                  </>
                )}
              </>
            ) : (
              <p>Loading...</p>
            )}
        </div>
        <div className='imageAndBut'>
            {profileImage && (
              <img src={profileImage.replace(/"/g, '')}
                  alt='Profile'
                  className='photo'
              />
            )} 
            
            <input type="file" accept='image/*' onChange={handleFileChange}/>
            <button onClick={handleUpload}>Upload</button>
        </div>
      
      
    </div>
    </>
  );

};

export default observer(Profile);