import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { userListBroker } from '../../features/userSlice';
import { Table, Spinner, Alert } from 'react-bootstrap';
import ReactPaginate from 'react-paginate';

const BrokerListPage = () => {
    const dispatch = useDispatch();
    const { userList, status, error } = useSelector(state => state.user);

    const [currentPage, setCurrentPage] = useState(0);
    const itemsPerPage = 10;

    useEffect(() => {
        dispatch(userListBroker());
    }, [dispatch]);

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

    // Sayfalama işlemi
    const offset = currentPage * itemsPerPage;
    const paginatedBrokers = userList.slice(offset, offset + itemsPerPage);
    const pageCount = Math.ceil(userList.length / itemsPerPage);

    return (
        <div>
            <h1 style={{ textAlign: 'center', marginBottom: '20px' }}>Broker Listesi</h1>
            {userList.length === 0 ? (
                <p style={{ textAlign: 'center' }}>Hiç broker bulunamadı.</p>
            ) : (
                <>
                    <Table striped bordered hover responsive>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Kullanıcı Adı</th>
                                <th>Email</th>
                                <th>Adı</th>
                                <th>Soyadı</th>
                                <th>Oluşturulma Tarihi</th>
                            </tr>
                        </thead>
                        <tbody>
                            {paginatedBrokers.map(broker => (
                                <tr key={broker.id}>
                                    <td>{broker.id}</td>
                                    <td>{broker.username}</td>
                                    <td>{broker.email}</td>
                                    <td>{broker.firstName}</td>
                                    <td>{broker.lastName}</td>
                                    <td>{broker.createdAt}</td>
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

export default BrokerListPage;
