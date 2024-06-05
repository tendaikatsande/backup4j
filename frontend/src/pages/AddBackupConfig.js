import axios from "axios";
import { useForm } from "react-hook-form";
import { toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {useState} from "react";

function AddBackupConfig() {
    const { register, handleSubmit, formState: { errors } } = useForm();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const onSubmit = async (data) => {
        setLoading(true);
        setError("");
        try {
            const response = await axios.post("/backup-config", data, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            toast.success("Backup configuration created successfully!");
        } catch (e) {
            console.error(e);
            setError("Failed to create backup configuration");
            toast.error("Failed to create backup configuration");
        } finally {
            setLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            {error && <div className="alert alert-danger">{error}</div>}

            <div className="form-group">
                <label htmlFor="host">Host</label>
                <input
                    type="text"
                    className="form-control"
                    id="host"
                    {...register("host", {required: true})}
                />
                {errors.host && <span className="text-danger">Host is required</span>}
            </div>
            <div className="form-group">
                <label htmlFor="port">Port</label>
                <input
                    type="number"
                    className="form-control"
                    id="port"
                    {...register("port", {required: true, valueAsNumber: true})}
                />
                {errors.port && <span className="text-danger">Port is required</span>}
            </div>
            <div className="form-group">
                <label htmlFor="username">Username</label>
                <input
                    type="text"
                    className="form-control"
                    id="username"
                    {...register("username", {required: true})}
                />
                {errors.username && <span className="text-danger">Username is required</span>}
            </div>
            <div className="form-group">
                <label htmlFor="password">Password</label>
                <input
                    type="password"
                    className="form-control"
                    id="password"
                    {...register("password", {required: true})}
                />
                {errors.password && <span className="text-danger">Password is required</span>}
            </div>
            <div className="form-group">
                <label htmlFor="format">Format</label>
                <input
                    type="text"
                    className="form-control"
                    id="format"
                    {...register("format", {required: true})}
                />
                {errors.format && <span className="text-danger">Format is required</span>}
            </div>
            <div className="form-check">
                <input
                    type="checkbox"
                    className="form-check-input"
                    id="withMetadata"
                    {...register("withMetadata")}
                />
                <label className="form-check-label" htmlFor="withMetadata">With Metadata</label>
            </div>
            <div className="form-group">
                <label htmlFor="backupPath">Backup Path</label>
                <input
                    type="text"
                    className="form-control"
                    id="backupPath"
                    {...register("backupPath", {required: true})}
                />
                {errors.backupPath && <span className="text-danger">Backup Path is required</span>}
            </div>
            <div className="form-group">
                <label htmlFor="databaseName">Database Name</label>
                <input
                    type="text"
                    className="form-control"
                    id="databaseName"
                    {...register("databaseName", {required: true})}
                />
                {errors.databaseName && <span className="text-danger">Database Name is required</span>}
            </div>
            <button type="submit" className="btn btn-outline-primary" disabled={loading}>
                {loading ? 'Loading...' : 'Submit'}
            </button>
        </form>

    );
}

export default AddBackupConfig;
