import { Spinner } from "react-bootstrap";

export const GeneralSpinner = () => {
    return (
        <div className="d-flex justify-content-center align-items-center" style={{height: '100vh'}}>
        <Spinner animation="border" variant="primary" />
        </div>
    );
};