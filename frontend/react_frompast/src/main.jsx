import {ChakraProvider, createStandaloneToast} from '@chakra-ui/react'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Login from "./components/login/Login.jsx";
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import React from 'react'
import AuthProvider from "./components/context/AuthContext.jsx";
import './index.css'


const {ToastContainer} = createStandaloneToast()
const router = createBrowserRouter([
    {
        path: "/",
        element: <Login/>
    },
    {
        path: "/dashboard",
        element: <App/>
    }
])

ReactDOM.createRoot(document.getElementById('root'))
    .render(
        <React.StrictMode>
            <ChakraProvider>
                <AuthProvider>
                    <RouterProvider router={router}/>
                </AuthProvider>
                <ToastContainer/>
            </ChakraProvider>
        </React.StrictMode>,
    )
