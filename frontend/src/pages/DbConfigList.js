import axios from "axios";
import {useEffect, useState} from "react";
import {FaArrowLeft, FaPlusCircle, FaServer} from "react-icons/fa";
import {FaLink} from "react-icons/fa6";
import {AiFillFastBackward} from "react-icons/ai";
import {useNavigate} from "react-router-dom";
import Modal from "../helpers/Modal";
import AddBackupConfig from "./AddBackupConfig";
import {toast} from "react-toastify";

function DbConfigList() {

    const PAGE_SIZE = 10;
    const [configs, setConfigs] = useState({});
    const [loading, setLoading] = useState({});
    const [error, setError] = useState("");
    const [pageRequest, setPageRequest] = useState({page: 0, size: PAGE_SIZE, sort: 'id,asc'});
    const [addConfig, setAddConfig] = useState(false)
    const navigate = useNavigate();

    const fetchAllBackupConfigs = async ({page, size, sort}) => {
        try {
            const res = await axios.get(`/backup-config?page=${page}&size=${size}&sort=${sort}`);
            setConfigs(res.data);
        } catch (e) {
            console.error(e);
            setError("Failed to fetch backup configs");
        }
    };

    const doBackup = async (payload) => {
        try {
            return await axios.post("/backups/create", payload);
        } catch (e) {
            console.error(e);
            throw new Error("Backup failed");
        }
    };

    useEffect(() => {
        fetchAllBackupConfigs(pageRequest);
    }, [pageRequest]);

    const handleBackup = async (payload) => {
        setLoading(prev => ({...prev, [payload.id]: true}));
        try {
            await doBackup(payload);
            toast.success("Backup successful!");
        } catch (e) {
            console.log(e);
            toast.error(e.message);
        } finally {
            setLoading(prev => ({...prev, [payload.id]: false}));
        }
    };

    const handlePageChange = (newPage) => {
        setPageRequest(prev => ({...prev, page: newPage}));
    };

    return (
        <div className="container">
            {addConfig && <Modal id={"addConfig"} title={"Add Backup Config"} show={addConfig}
                                 onClose={() => setAddConfig(false)}><AddBackupConfig/></Modal>}

            <div className="py-4">
                <div className="table-wrapper">
                    <div className="table-title">
                        <div className="row">
                            <div className="col-xs-6">
                                <h2>Manage <b>Database Configs</b></h2>
                            </div>
                            <div className="col-xs-6">
                                <button className="btn btn-outline-success" onClick={() => navigate("/")}>
                                    <FaArrowLeft/> <span>Back</span>
                                </button>
                                <button className="btn btn-outline-success" onClick={() => setAddConfig(!addConfig)}>
                                    <FaPlusCircle/> <span>Add New Config</span>
                                </button>
                            </div>
                        </div>
                    </div>
                    {error && <div className="alert alert-danger">{error}</div>}
                    <table className="table table-striped table-hover">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Host</th>
                            <th>Port</th>
                            <th>Name</th>
                            <th>User</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {configs.content?.map(config => (
                            <tr key={config.id}>
                                <td>{config.id}</td>
                                <td>{config.host}</td>
                                <td>{config.port}</td>
                                <td>{config.databaseName}</td>
                                <td>{config.username}</td>
                                <td>
                                    <button className="btn btn-success" onClick={() => handleBackup({backConfigId:config.id})}
                                            disabled={loading[config.id] ?? false}>
                                        {loading[config.id] ? 'Loading...' : <><FaServer/> Backup</>}
                                    </button>
                                    <button className="btn btn-info">
                                        <FaLink/> Link
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                    <div className="clearfix">
                        <div className="hint-text">Showing <b>{pageRequest.page + 1}</b> out
                            of <b>{configs.totalPages}</b> pages
                        </div>
                        <div>
                            <button className="btn" onClick={() => handlePageChange(pageRequest.page - 1)}
                                    disabled={configs.first}>Previous
                            </button>
                            <button className="btn" onClick={() => handlePageChange(pageRequest.page + 1)}
                                    disabled={configs.last}>Next
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default DbConfigList;
