import './LoginForm.css';
import React, {FC, useContext, useState, useEffect } from 'react';
import { Context } from '../../index';
import {observer} from "mobx-react-lite";
import { useNavigate } from 'react-router-dom';

const LoginForm: FC = () => {
    const [email, setEmail] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const { store } = useContext(Context);
    const navigate = useNavigate();

    
    
    const handleRegistration = async () => {
        try {
            await store.registration(email, password);
            setSuccessMessage('Registration successful!');
            
           
        } catch (error) {
            console.error('Error during registration:', error);
            setSuccessMessage('Registration failed. Please try again.');
        }
    };

    const handleLogin = async () => {
        try {
            await store.login(email, password);
            console.log("registration success");
            navigate('/general');
            
           
        } catch (error) {
            console.error('Error during login:', error);
            setSuccessMessage('Invalid username or password.');
        }
    };

    

    return (

        <div className='container'>
            <h1>MATCHA</h1>
            <input 
                onChange={e => setEmail(e.target.value)}
                value={email}
                type='text' 
                placeholder='email'
            />
            <input 
                onChange={e => setPassword(e.target.value)}
                value={password}
                type='password' 
                placeholder='password'
            />
            <button onClick = {handleLogin}>LOGIN</button>
            <button onClick = {handleRegistration}>REGISTRATION</button>

            {successMessage && <p>{successMessage}</p>}
        </div>
    );

};

export default observer(LoginForm);