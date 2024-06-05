import { useEffect, useState } from "react";
import { toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import { FaDatabase, FaServer, FaCog, FaExclamationCircle } from 'react-icons/fa';
import { Spinner } from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import {useNavigate} from "react-router-dom";
import axios from "axios";

function App() {
    const [serverStatus, setServerStatus] = useState("");
    const [backupCount, setBackupCount] = useState(0);
    const [configCount, setConfigCount] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const fetchServerStatus = async () => {
        try {
            const response = await axios.get("/actuator/health");
            setServerStatus(response.data.status);
        } catch (e) {
            console.error(e);
            setError("Failed to fetch server status");
            toast.error("Failed to fetch server status!");
        }
    };

    const fetchBackupData = async () => {
        try {
            const response = await axios.get("/backup-config");
            const data = response.data;
            setConfigCount(data.totalElements);  // Assuming totalElements represents the count of configs
        } catch (e) {
            console.error(e);
            setError("Failed to fetch backup data");
            toast.error("Failed to fetch backup data!");
        }
    };
    const fetchBackups = async () => {
        try {
            const response = await axios.get("/backups");
            const data = response.data;
            setBackupCount(data.totalElements);  // Assuming totalElements represents the count of backups done
        } catch (e) {
            console.error(e);
            setError("Failed to fetch backup data");
            toast.error("Failed to fetch backup data!");
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            await fetchServerStatus();
            await fetchBackupData();
            await fetchBackups()
            setLoading(false);
        };
        fetchData();
    }, []);

    if (loading) {
        return (
            <div className="container text-center">
                <Spinner style={{ width: '3rem', height: '3rem' }} />
                <div>Loading...</div>
            </div>
        );
    }

    return (
        <div className="container mt-5">
            <ToastContainer />
            <h1 className="mb-4">Dashboard</h1>
            {error && <div className="alert alert-danger"><FaExclamationCircle /> {error}</div>}
            <div className="row">
                <div className="col-md-4 mb-4">
                    <div className="card text-center shadow-sm">
                        <div className="card-body">
                            <FaServer className="mb-3" size={50} />
                            <h5 className="card-title">Server Status</h5>
                            <p className="card-text">{serverStatus}</p>
                        </div>
                    </div>
                </div>
                <div className="col-md-4 mb-4">
                    <div className="card text-center shadow-sm" onClick={()=>navigate("/backups") }>
                        <div className="card-body">
                            <FaDatabase className="mb-3" size={50} />
                            <h5 className="card-title">Database Backups Done</h5>
                            <p className="card-text">{backupCount}</p>
                        </div>
                    </div>
                </div>
                <div className="col-md-4 mb-4">
                    <div className="card text-center shadow-sm" onClick={()=>navigate("/configs") }>
                        <div className="card-body">
                            <FaCog className="mb-3" size={50} />
                            <h5 className="card-title">Number of Configs</h5>
                            <p className="card-text">{configCount}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default App;
