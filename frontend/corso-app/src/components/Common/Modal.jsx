

const Modal = ({ type, title, message, isOpen, onClose, onConfirm, onHide }) => {
    console.log("MODAL: ", isOpen)
    return (
        <div className="modal fade" id="staticBackdrop" show={isOpen} onHide={onHide} data-bs-backdrop="static" data-bs-keyboard="false" tabIndex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h1 className="modal-title fs-5" id="staticBackdropLabel">{title}</h1>
                        <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close" onClick={onClose}></button>
                    </div>
                    <div className="modal-body">
                        {message}
                    </div>
                    <div className="modal-footer">
                        {type === 'alert' && <button type="button" className="btn btn-primary" data-bs-dismiss="modal" onClick={onClose}>Close</button>}
                        {type === 'confirm' && <button type="button" className="btn btn-primary" data-bs-dismiss="modal" onClick={onConfirm}>Confirm</button>}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Modal;