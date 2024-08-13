import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchUserList, deleteUser, activateUser, unblockUser } from '../../features/userSlice';
import { Button, Table, Spinner, Alert } from 'react-bootstrap';
import ReactPaginate from 'react-paginate';

const UserList = () => {
    const dispatch = useDispatch();
    const { userList, status, error } = useSelector(state => state.user);

    const [currentPage, setCurrentPage] = useState(0);
    const [alert, setAlert] = useState({ show: false, message: '', variant: '' });  // Hata ve başarı mesajlarını saklamak için kullanılan state
    const itemsPerPage = 5;

    useEffect(() => {
        dispatch(fetchUserList());
    }, [dispatch]);

    const handleDelete = (userId) => {
        if (window.confirm('Bu kullanıcıyı silmek istediğinizden emin misiniz?')) {
            dispatch(deleteUser({ userId }))
                .unwrap()
                .then(() => {
                    setAlert({ show: true, message: 'Kullanıcı başarıyla silindi.', variant: 'success' });
                })
                .catch((err) => {
                    setAlert({ show: true, message: `Hata: ${err.message}`, variant: 'danger' });
                });
        }
    };

    const handleActivate = (email) => {
        dispatch(activateUser({ email }))
            .unwrap()
            .then(() => {
                setAlert({ show: true, message: 'Kullanıcı başarıyla aktifleştirildi.', variant: 'success' });
            })
            .catch((err) => {
                setAlert({ show: true, message: `Hata: ${err.message}`, variant: 'danger' });
            });
    };

    const handleUnblock = (email) => {
        dispatch(unblockUser({ email }))
            .unwrap()
            .then(() => {
                setAlert({ show: true, message: 'Kullanıcının engeli başarıyla kaldırıldı.', variant: 'success' });
            })
            .catch((err) => {
                setAlert({ show: true, message: `Hata: ${err.message}`, variant: 'danger' });
            });
    };

    const handlePageChange = ({ selected }) => {
        setCurrentPage(selected);
    };

    const formatDate = (dateArray) => {
        if (!dateArray) return 'N/A';
        const [year, month, day, hour, minute, second, millisecond] = dateArray;
        return new Date(Date.UTC(year, month - 1, day, hour, minute, second, millisecond)).toLocaleString('tr-TR');
    };

    if (status === 'loading') {
        return (
            <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
                <Spinner animation="border" variant="primary" />
            </div>
        );
    }

    if (status === 'failed') {
        return <Alert variant="danger">Hata: {error}</Alert>;
    }

    const paginatedUsers = userList.slice(currentPage * itemsPerPage, (currentPage + 1) * itemsPerPage);
    const pageCount = Math.ceil(userList.length / itemsPerPage);

    return (
        <div>
            <h1 className="text-center mb-4">Kullanıcılar</h1>
            {alert.show && <Alert variant={alert.variant} onClose={() => setAlert({ show: false })} dismissible>{alert.message}</Alert>}
            {userList.length === 0 ? (
                <p className="text-center">Kullanıcı bulunamadı.</p>
            ) : (
                <>
                    <Table striped bordered hover responsive className="table">
                        <thead>
                            <tr>
                                <th style={{ width: '10%' }}>ID</th>
                                <th style={{ width: '10%' }}>Kullanıcı Adı</th>
                                <th style={{ width: '10%' }}>İsim</th>
                                <th style={{ width: '10%' }}>Soyisim</th>
                                <th style={{ width: '10%' }}>Email</th>
                                <th style={{ width: '10%' }}>Telefon Numarası</th>
                                <th style={{ width: '10%' }}>Oluşturulma Tarihi</th>
                                <th style={{ width: '10%' }}>Güncelleme Tarihi</th>
                                <th style={{ width: '20%' }}>İşlemler</th>
                            </tr>
                        </thead>
                        <tbody>
                            {paginatedUsers.map(user => (
                                <tr key={user.id}>
                                    <td>{user.id}</td>
                                    <td>{user.username || 'N/A'}</td>
                                    <td>{user.firstName || 'N/A'}</td>
                                    <td>{user.lastName || 'N/A'}</td>
                                    <td>{user.email}</td>
                                    <td>{user.phone || 'N/A'}</td>
                                    <td>{formatDate(user.createdAt)}</td>
                                    <td>{formatDate(user.updatedAt)}</td>
                                    <td>
                                        <div className="d-flex gap-2">
                                            <Button variant="warning btn flex-grow-1" onClick={() => handleActivate(user.email)}>Aktifleştir</Button>
                                            <Button variant="danger btn flex-grow-1" onClick={() => handleDelete(user.id)}>Sil</Button>
                                            <Button variant="info btn flex-grow-1 text-nowrap" onClick={() => handleUnblock(user.email)}>Engeli Kaldır</Button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                    <ReactPaginate
                        previousLabel={"Önceki"}
                        nextLabel={"Sonraki"}
                        breakLabel={"..."}
                        pageCount={pageCount}
                        marginPagesDisplayed={2}
                        pageRangeDisplayed={5}
                        onPageChange={handlePageChange}
                        containerClassName={"pagination d-flex justify-content-center mt-4"}
                        pageClassName={"page-item"}
                        pageLinkClassName={"page-link"}
                        previousClassName={"page-item"}
                        previousLinkClassName={"page-link"}
                        nextClassName={"page-item"}
                        nextLinkClassName={"page-link"}
                        breakClassName={"page-item"}
                        breakLinkClassName={"page-link"}
                        activeClassName={"active"}
                    />
                </>
            )}
        </div>
    );
};

export default UserList;
