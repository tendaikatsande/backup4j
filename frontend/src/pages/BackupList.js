import axios from "axios";
import {useEffect, useState} from "react";
import {FaArrowLeft, FaPlusCircle, FaServer} from "react-icons/fa";
import {FaLink} from "react-icons/fa6";
import {AiFillFastBackward} from "react-icons/ai";
import {useNavigate} from "react-router-dom";
import Modal from "../helpers/Modal";
import AddBackupConfig from "./AddBackupConfig";

function DbConfigList() {

    const PAGE_SIZE = 10;
    const [configs, setConfigs] = useState({});
    const [loading, setLoading] = useState({});
    const [error, setError] = useState("");
    const [pageRequest, setPageRequest] = useState({page: 0, size: PAGE_SIZE, sort: 'id,asc'});
    const [addConfig, setAddConfig] = useState(false)
    const navigate = useNavigate();

    const fetchAllBackups = async ({page, size, sort}) => {
        try {
            const res = await axios.get(`/backups?page=${page}&size=${size}&sort=${sort}`);
            setConfigs(res.data);
        } catch (e) {
            console.error(e);
            setError("Failed to fetch backup configs");
        }
    };


    useEffect(() => {
        fetchAllBackups(pageRequest);
    }, [pageRequest]);


    const handlePageChange = (newPage) => {
        setPageRequest(prev => ({...prev, page: newPage}));
    };

    return (
        <div className="container">
            {addConfig && <Modal><AddBackupConfig/></Modal>}

            <div className="py-4">
                <div className="table-wrapper">
                    <div className="table-title">
                        <div className="row">
                            <div className="col-xs-6">
                                <h2>List <b>Database Backups</b></h2>
                            </div>
                            <div className="col-xs-6">
                                <button className="btn btn-outline-success" onClick={() => navigate("/")}>
                                    <FaArrowLeft/> <span>Back</span>
                                </button>

                            </div>
                        </div>
                    </div>
                    {error && <div className="alert alert-danger">{error}</div>}
                    <table className="table table-striped table-hover">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Database</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {configs.content?.map(backup => (
                            <tr key={backup.id}>
                                <td>{backup.id}</td>
                                <td>
                                    {backup.backupConfig?.host}:
                                    {backup.backupConfig?.port}/
                                    {backup.backupConfig?.databaseName}
                                </td>
                                <td>
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
