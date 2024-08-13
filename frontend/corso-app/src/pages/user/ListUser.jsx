import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchUserList, deleteUser, activateUser, unblockUser } from '../../features/userSlice';
import { Button, Table, Spinner, Alert } from 'react-bootstrap';
import moment from 'moment';
import ReactPaginate from 'react-paginate';

const UserList = () => {
    const dispatch = useDispatch();
    const { userList, status, error } = useSelector(state => state.user);

    const [currentPage, setCurrentPage] = useState(0);
    const itemsPerPage = 5; // Sayfada gösterilecek kullanıcı sayısını 5 olarak ayarladık

    useEffect(() => {
        dispatch(fetchUserList());
    }, [dispatch]);

    const handleDelete = (userId) => {
        if (window.confirm('Are you sure you want to delete this user?')) {
            dispatch(deleteUser({ userId }));
        }
    };

    const convertToDate = (dateArray) => {
        if (!Array.isArray(dateArray) || dateArray.length < 7) {
            return null; // Bozuk veya eksik tarih verisi
        }

        const [year, month, day, hour, minute, second, millisecond] = dateArray;
        // Month değeri 0-11 arasında olduğu için +1 ekliyoruz
        return new Date(year, month - 1, day, hour, minute, second, millisecond);
    };

    const handleActivate = (email) => {
        dispatch(activateUser({ email }));
    };

    const handleUnblock = (email) => {
        dispatch(unblockUser({ email }));
    };

    const handlePageChange = ({ selected }) => {
        setCurrentPage(selected);
    };

    if (status === 'loading') {
        return (
            <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
                <Spinner animation="border" variant="primary" />
            </div>
        );
    }

    if (status === 'failed') {
        return <Alert variant="danger">Error: {error}</Alert>;
    }

    const paginatedUsers = userList.slice(currentPage * itemsPerPage, (currentPage + 1) * itemsPerPage);
    const pageCount = Math.ceil(userList.length / itemsPerPage);

    return (
        <div>
            <h1 style={{ textAlign: 'center', marginBottom: '50px' }}>Kullanıcılar</h1>
            {userList.length === 0 ? (
                <p style={{ textAlign: 'center' }}>No users found.</p>
            ) : (
                <>
                    <Table striped bordered hover responsive>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Kullanıcı Adı</th>
                                <th>İsim</th>
                                <th>Soyisim</th>
                                <th>Email</th>
                                <th>Telefon Numarası</th>
                                <th>Oluşturulduğu Tarih</th>
                                <th>Güncellendiği Tarih</th>
                                <th>Durum</th>
                                <th style={{ minWidth: '200px' }}>İşlemler</th>
                            </tr>
                        </thead>
                        <tbody>
                            {paginatedUsers.map(user => (
                                <tr key={user.id}>
                                    <td>{user.id}</td>
                                    <td>{user.username}</td>
                                    <td>{user.firstName}</td>
                                    <td>{user.lastName}</td>
                                    <td>{user.email}</td>
                                    <td>{user.phone}</td>
                                    <td>
                                        {user.createdAt 
                                            ? moment(convertToDate(user.createdAt)).format('YYYY-MM-DD HH:mm:ss')
                                            : 'Tarih Bilgisi Yok'}
                                    </td>
                                    <td>
                                        {user.updatedAt 
                                            ? moment(convertToDate(user.updatedAt)).format('YYYY-MM-DD HH:mm:ss')
                                            : 'Tarih Bilgisi Yok'}
                                    </td>
                                    <td>
                                        {user.accountLocked ? 'Blokeli' : 'Aktif'}<br />
                                        {user.isDeleted ? 'Silinmiş' : 'Silinmemiş'}
                                    </td>
                                    <td>
                                        <Button
                                            variant="success"
                                            onClick={() => handleActivate(user.email)}
                                            disabled={!user.accountLocked || user.isDeleted}
                                            style={{ marginRight: '5px', width: '120px' }}
                                        >
                                            Aktif Et
                                        </Button>
                                        <Button
                                            variant="warning"
                                            onClick={() => handleUnblock(user.email)}
                                            disabled={!user.accountLocked || user.isDeleted}
                                            style={{ marginRight: '5px', width: '120px' }}
                                        >
                                            Bloke Kaldır
                                        </Button>
                                        <Button
                                            variant="danger"
                                            onClick={() => handleDelete(user.id)}
                                            disabled={user.isDeleted}
                                            style={{ width: '120px' }}
                                        >
                                            Sil
                                        </Button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                    <div className="d-flex justify-content-center">
                        <ReactPaginate
                            previousLabel={'Önceki'}
                            nextLabel={'Sonraki'}
                            breakLabel={'...'}
                            pageCount={pageCount}
                            marginPagesDisplayed={2}
                            pageRangeDisplayed={5}
                            onPageChange={handlePageChange}
                            containerClassName={'pagination'}
                            pageClassName={'page-item'}
                            pageLinkClassName={'page-link'}
                            previousClassName={'page-item'}
                            previousLinkClassName={'page-link'}
                            nextClassName={'page-item'}
                            nextLinkClassName={'page-link'}
                            breakClassName={'page-item'}
                            breakLinkClassName={'page-link'}
                            activeClassName={'active'}
                        />
                    </div>
                </>
            )}
        </div>
    );
};

export default UserList;
