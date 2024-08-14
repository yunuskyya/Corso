import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchUserList, deleteUser, activateUser, unblockUser } from '../../features/userSlice';
import { Button, Table, Spinner, Alert, Form } from 'react-bootstrap';
import ReactPaginate from 'react-paginate';
import moment from 'moment';  

const UserList = () => {
    const dispatch = useDispatch();
    const { userList, status, error } = useSelector(state => state.user);

    const [currentPage, setCurrentPage] = useState(0);
    const [alert, setAlert] = useState({ show: false, message: '', variant: '' });
    const [searchTerm, setSearchTerm] = useState('');
    const [filteredUsers, setFilteredUsers] = useState([]);
    const [filterState, setFilterState] = useState('active'); // User filter state
    const itemsPerPage = 5;

    useEffect(() => {
        dispatch(fetchUserList());
    }, [dispatch]);
    
    useEffect(() => {
        console.log('*****UserList component mounted', userList);
        const filtered = userList.filter(user => {
            const email = user.email || '';
            const isMatch = email.toLowerCase().includes(searchTerm.toLowerCase());
    
            if (filterState === 'all') {
                return isMatch; // Show all users
            }
    
            if (filterState === 'active') {
                return isMatch && !user.deleted && !user.accountLocked;
            }
    
            if (filterState === 'deleted') {
                return isMatch && user.deleted;
            }
    
            if (filterState === 'blocked') {
                return isMatch && user.accountLocked;
            }
    
            return false; 
        });
        setFilteredUsers(filtered);
    }, [searchTerm, userList, filterState]);

    const handleDelete = (user) => {
        if (window.confirm('Bu kullanıcıyı silmek istediğinizden emin misiniz?')) {
            dispatch(deleteUser({ userId: user.id }))
                .unwrap()
                .then(() => {
                    setAlert({ show: true, message: 'Kullanıcı başarıyla silindi.', variant: 'success' });
                    dispatch(fetchUserList());
                })
                .catch((err) => {
                    setAlert({ show: true, message: `Hata: ${err.message}`, variant: 'danger' });
                });
        }
    };

    const handleActivate = (user) => {
        dispatch(activateUser({ email: user.email }))
            .unwrap()
            .then(() => {
                setAlert({ show: true, message: 'Kullanıcı başarıyla aktifleştirildi.', variant: 'success' });
                dispatch(fetchUserList());
            })
            .catch((err) => {
                setAlert({ show: true, message: `Hata: ${err.message}`, variant: 'danger' });
            });
    };

    const handleUnblock = (user) => {
        dispatch(unblockUser({ email: user.email }))
            .unwrap()
            .then(() => {
                setAlert({ show: true, message: 'Kullanıcı başarıyla engellemeyi kaldırdı.', variant: 'success' });
                dispatch(fetchUserList());
            })
            .catch((err) => {
                setAlert({ show: true, message: `Hata: ${err.message}`, variant: 'danger' });
            });
    };

    const handlePageChange = ({ selected }) => {
        setCurrentPage(selected);
    };
  
    const formatDate = (dateArray) => {
        if (dateArray) {
            const [year, month, day, hours, minutes, seconds, nanoseconds] = dateArray;
            const date = new Date(year, month - 1, day, hours, minutes, seconds, Math.floor(nanoseconds / 1000000));
            const momentDate = moment(date);
            if (!momentDate.isValid()) {
                return 'Geçersiz Tarih';
            }
            return momentDate.format('DD-MM-YYYY HH:mm:ss'); // Customize format as needed
        }
        return '';
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

    // Calculate pagination
    const startIndex = currentPage * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const paginatedUsers = filteredUsers.slice(startIndex, endIndex);
    const pageCount = Math.min(Math.ceil(filteredUsers.length / itemsPerPage), 10); 

    return (
        <div className="container mt-4">
            {alert.show && <Alert variant={alert.variant} onClose={() => setAlert({ show: false })} dismissible>{alert.message}</Alert>}
            <Form className="mb-4">
                <Form.Group controlId="search">
                    <Form.Control
                        type="text"
                        placeholder="E-posta ile ara..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </Form.Group>
            </Form>
            <div className="mb-4">
                <Button
                    variant={filterState === 'all' ? "primary" : "secondary"}
                    onClick={() => setFilterState('all')}
                    className="me-2"
                >
                    Tüm Kullanıcıları Göster
                </Button>
                <Button
                    variant={filterState === 'active' ? "primary" : "secondary"}
                    onClick={() => setFilterState('active')}
                    className="me-2" 
                >
                    Aktif Kullanıcıları Göster
                </Button>
                <Button
                    variant={filterState === 'deleted' ? "primary" : "secondary"}
                    onClick={() => setFilterState('deleted')}
                    className="me-2"
                >
                    Silinmiş Kullanıcıları Göster
                </Button>
                <Button
                    variant={filterState === 'blocked' ? "primary" : "secondary"}
                    onClick={() => setFilterState('blocked')}
                >
                    Engellenmiş Kullanıcıları Göster
                </Button>
            </div>
            {filteredUsers.length === 0 ? (
                <p className="text-center">Kullanıcı bulunamadı.</p>
            ) : (
                <>
                    <div className="table-responsive">
                        <Table striped bordered hover>
                            <thead className="thead-dark">
                                <tr>
                                    <th style={{ width: '5%' }}>ID</th>
                                    <th style={{ width: '15%' }}>Kullanıcı Adı</th>
                                    <th style={{ width: '10%' }}>Ad</th>
                                    <th style={{ width: '10%' }}>Soyad</th>
                                    <th style={{ width: '10%' }}>E-posta</th>
                                    <th style={{ width: '10%' }}>Telefon</th>
                                    <th style={{ width: '15%' }}>Oluşturulma Tarihi</th>
                                    <th style={{ width: '15%' }}>Güncellenme Tarihi</th>
                                    <th style={{ width: '10%' }}>İşlemler</th>
                                </tr>
                            </thead>
                            <tbody>
                                {paginatedUsers.map(user => (
                                    <tr key={user.id}>
                                        <td>{user.id}</td>
                                        <td>{user.username || ''}</td>
                                        <td>{user.firstName || ''}</td>
                                        <td>{user.lastName || ''}</td>
                                        <td>{user.email}</td>
                                        <td>{user.phone || ''}</td>
                                        <td>{formatDate(user.createdAt)}</td>
                                        <td>{formatDate(user.updatedAt)}</td>
                                        <td>
                                            {filterState === 'active' && (
                                                <Button
                                                    variant="danger"
                                                    onClick={() => handleDelete(user)}
                                                    size="sm"
                                                    className="me-2" 
                                                    style={{ padding: '5px 40px', fontSize: '16px' }} 
                                                >
                                                    Sil
                                                </Button>
                                            )}
                                            {filterState === 'deleted' && (
                                                <Button
                                                    variant="success"
                                                    onClick={() => handleActivate(user)}
                                                    size="sm"
                                                    className="me-2" 
                                                >
                                                    Aktifleştir
                                                </Button>
                                            )}
                                            {filterState === 'blocked' && (
                                                <Button
                                                    variant="info"
                                                    onClick={() => handleUnblock(user)}
                                                    size="sm"
                                                >
                                                    Engellemeyi Kaldır
                                                </Button>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                    </div>
                    <ReactPaginate
                        previousLabel={"Önceki"}
                        nextLabel={"Sonraki"}
                        breakLabel={"..."}
                        pageCount={pageCount}
                        marginPagesDisplayed={2}
                        pageRangeDisplayed={5}
                        onPageChange={handlePageChange}
                        containerClassName={"pagination justify-content-center"}
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
