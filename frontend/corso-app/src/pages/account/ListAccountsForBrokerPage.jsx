import { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { getAllAccountsForBrokerById } from '../../features/accountSlice';
import { Button, Table, Spinner, Alert } from 'react-bootstrap';
import moment from 'moment';
import ReactPaginate from 'react-paginate';
import useAuth from '../../hooks/useAuth';

const ListAccountsForBrokerrPage = () => {
    const dispatch = useAppDispatch();
    const { accounts, status, error } = useAppSelector(state => state.account);
    const { user } = useAuth();

    const [currentPage, setCurrentPage] = useState(0);
    const itemsPerPage = 10;

    useEffect(() => {
        dispatch(getAllAccountsForBrokerById(user?.id));
    }, [dispatch]);

    const handlePageChange = ({ selected }) => {
        setCurrentPage(selected);
    };

    const convertToDate = (dateArray) => {
        if (!Array.isArray(dateArray) || dateArray.length < 7) {
            return null; // Bozuk veya eksik tarih verisi
        }

        const [year, month, day, hour, minute, second, millisecond] = dateArray;
        // Month değeri 0-11 arasında olduğu için +1 ekliyoruz
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
        return <Alert variant="danger">Error: {error}</Alert>;
    }

    // Paginate accounts
    const offset = currentPage * itemsPerPage;
    const paginatedAccounts = accounts.slice(offset, offset + itemsPerPage);
    const pageCount = Math.ceil(accounts.length / itemsPerPage);

    return (
        <div>
            <h1 style={{ textAlign: 'center', marginBottom: '20px' }}>Hesap Listesi</h1>
            {accounts.length === 0 ? (
                <p style={{ textAlign: 'center' }}>No accounts found.</p>
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
                                <th>Müşteri No</th>
                            </tr>
                        </thead>
                        <tbody>
                            {paginatedAccounts.map(account => (
                                <tr key={account.id}>
                                    <td>{account.id}</td>
                                    <td>{account.accountNumber}</td>
                                    <td>{account.currency}</td>
                                    <td>{account.balance}</td>
                                    <td>{moment(convertToDate(account.createdAt)).format('YYYY-MM-DD HH:mm:ss')}</td>
                                    <td>{moment(convertToDate(account.updatedAt)).format('YYYY-MM-DD HH:mm:ss')}</td>
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

export default ListAccountsForBrokerrPage;
