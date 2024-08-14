import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { getAllAccountsForManager } from '../../features/accountSlice';
import { Button, Table, Spinner, Alert, Form, InputGroup } from 'react-bootstrap';
import ReactPaginate from 'react-paginate';
import { format } from 'date-fns'; // Import format from date-fns

const ListAccountsForManagerPage = () => {
    const dispatch = useDispatch();
    const { accounts, status, error } = useSelector(state => state.account);

    const [currentPage, setCurrentPage] = useState(0);
    const [searchQuery, setSearchQuery] = useState('');
    const [filteredAccounts, setFilteredAccounts] = useState([]);
    const itemsPerPage = 10;

    useEffect(() => {
        dispatch(getAllAccountsForManager());
    }, [dispatch]);

    useEffect(() => {
        if (searchQuery) {
            const filtered = accounts.filter(account =>
                account.accountNumber.includes(searchQuery) ||
                account.currency.toLowerCase().includes(searchQuery.toLowerCase())
            );
            setFilteredAccounts(filtered);
        } else {
            setFilteredAccounts(accounts);
        }
    }, [searchQuery, accounts]);

    const handlePageChange = ({ selected }) => {
        setCurrentPage(selected);
    };

    const handleSearchChange = (e) => {
        setSearchQuery(e.target.value);
    };

    // Function to convert date array to readable date
    const formatDate = (dateArray) => {
        if (!Array.isArray(dateArray) || dateArray.length < 7) {
            return 'Geçersiz Tarih'; // Invalid Date
        }

        const [year, month, day, hours, minutes, seconds, nanoseconds] = dateArray;
        // Create a new Date object with correct zero-based month adjustment
        // Convert nanoseconds to milliseconds
        const date = new Date(year, month - 1, day, hours, minutes, seconds, Math.floor(nanoseconds / 1000000));

        // Format the date as desired using date-fns
        return format(date, 'dd-MM-yyyy HH:mm:ss'); // Customize format as needed
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

    // Paginate accounts
    const offset = currentPage * itemsPerPage;
    const paginatedAccounts = filteredAccounts.slice(offset, offset + itemsPerPage);
    const pageCount = Math.ceil(filteredAccounts.length / itemsPerPage);

    return (
        <div>
            <h1 style={{ textAlign: 'center', marginBottom: '20px' }}>Hesap Listesi</h1>
            <div className="mb-4">
                <InputGroup className="mb-3">
                    <Form.Control
                        type="text"
                        placeholder="Hesap Numarası veya Para Birimi ile Ara..."
                        value={searchQuery}
                        onChange={handleSearchChange}
                    />
                </InputGroup>
            </div>
            {filteredAccounts.length === 0 ? (
                <p style={{ textAlign: 'center' }}>Hiç hesap bulunamadı.</p>
            ) : (
                <>
                    <Table striped bordered hover responsive>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Hesap Numarası</th>
                                <th>Para Birimi</th>
                                <th>Bakiye</th>
                                <th>Oluşturulma Tarihi</th>
                                <th>Güncellenme Tarihi</th>
                                <th>Müşteri ID</th>
                            </tr>
                        </thead>
                        <tbody>
                            {paginatedAccounts.map(account => (
                                <tr key={account.id}>
                                    <td>{account.id}</td>
                                    <td>{account.accountNumber}</td>
                                    <td>{account.currency}</td>
                                    <td>{account.balance.toFixed(2)}</td>
                                    <td>{formatDate(account.createdAt)}</td>
                                    <td>{formatDate(account.updatedAt)}</td>        
                                    <td>{account.customerId}</td>
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

export default ListAccountsForManagerPage;
