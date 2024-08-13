import React, { useEffect, useState } from 'react';
import { Table, Spinner, Alert } from 'react-bootstrap';
import ReactPaginate from 'react-paginate';
import moment from 'moment';
import { userListBroker } from '../../api/userApi'; // API fonksiyonunun yolu

const ListBrokerPage = () => {
    const dispatch = useDispatch();
    const { userList, status, error } = useSelector(state => state.user);

    const [currentPage, setCurrentPage] = useState(0);
    const itemsPerPage = 10;

    useEffect(() => {
        const fetchBrokers = async () => {
            try {
                const data = await userListBroker();
                setBrokers(data.content || []);
                setStatus('succeeded');
            } catch (error) {
                setError('Failed to fetch broker list');
                setStatus('failed');
            }
        };
        fetchBrokers();
    }, []);

    const handlePageChange = ({ selected }) => {
        setCurrentPage(selected);
    };

    const convertToDate = (dateArray) => {
        if (!Array.isArray(dateArray) || dateArray.length < 7) {
            return null; // Bozuk veya eksik tarih verisi
        }

        const [year, month, day, hour, minute, second, millisecond] = dateArray;
        return new Date(year, month - 1, day, hour, minute, second, millisecond);
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

    const offset = currentPage * itemsPerPage;
    const paginatedBrokers = brokers.slice(offset, offset + itemsPerPage);
    const pageCount = Math.ceil(brokers.length / itemsPerPage);

    return (
        <div>
            <h1 style={{ textAlign: 'center', marginBottom: '20px' }}>Broker Listesi</h1>
            {brokers.length === 0 ? (
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
                                <th>Telefon Numarası</th>
                                <th>Güncelleme Tarihi</th>
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
                                    <td>{broker.phone}</td>
                                    <td>{formatDate(broker.updatedAt)}</td>
                                    <td>{formatDate(broker.createdAt)}</td>
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
const formatDate = (dateArray) => {

    if (dateArray) {
        const [year, month, day, hours, minutes, seconds, nanoseconds] = dateArray;

        // Create a new Date object with correct zero-based month adjustment
        // Convert nanoseconds to milliseconds
        const date = new Date(year, month - 1, day, hours, minutes, seconds, Math.floor(nanoseconds / 1000000));

        // Create a moment object from the Date object
        const momentDate = moment(date);

        // Check if the date is valid
        if (!momentDate.isValid()) {
            return 'Invalid Date';
        }

        // Format the date as desired
        return momentDate.format('DD-MM-YYYY HH:mm:ss'); // Customize format as needed

    }

    return 'NULL DATE'
};

export default ListBrokerPage;
