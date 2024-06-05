import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import DbConfigList from "./pages/DbConfigList";
import {ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import BackupList from "./pages/BackupList";

const root = ReactDOM.createRoot(document.getElementById('root'));

const router = createBrowserRouter([
    {
        path: "/",
        element: <App/>,
    },
    {
        path: "/configs",
        element: <DbConfigList/>,
    },
    {
        path: "/backups",
        element: <BackupList/>,
    },
]);

root.render(
    <React.StrictMode>
        <ToastContainer/>
        <RouterProvider router={router}/>
    </React.StrictMode>
);

reportWebVitals();
