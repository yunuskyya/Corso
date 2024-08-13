import { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { fetchFilteredCustomers } from '../../features/customerListSlice';
import { createNewAccount, resetState } from '../../features/createAccountSlice';
import { currencies } from '../../constants/currencies';
import useAuth from './../../hooks/useAuth';
import { useLocation } from 'react-router-dom';

const AddAccountPage = () => {
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);

    const [firstFetch, setFirstFetch] = useState(true);
    const dispatch = useAppDispatch();
    const { customers, totalPages, currentPage, loading, error } = useAppSelector((state) => state.customerList);
    const { success, error: createAccountError } = useAppSelector((state) => state.createAccount);

    const [selectedCustomerId, setSelectedCustomerId] = useState(null);
    const [currencyCode, setCurrencyCode] = useState('');
    const [searchCustomerId, setSearchCustomerId] = useState(queryParams.get('customerId') || '');
    const { user } = useAuth();

    useEffect(() => {
        console.log('HELLOOOO');
        if (searchCustomerId !== '') {
            console.log('Fetching customers with searchCustomerId:', searchCustomerId);
            dispatch(fetchFilteredCustomers({ filterRequest: { userId: user?.id, customerId: searchCustomerId }, page: 0, size: 5, sort: 'name' }));
            // Remove the query parameter after search
            window.history.replaceState(null, '', '/add-account');
        } else if (firstFetch) {
            console.log('Fetching customers for the first time...');
            dispatch(fetchFilteredCustomers({ filterRequest: { userId: user?.id }, page: 0, size: 5, sort: 'name' }));
            setFirstFetch(false);
        }
    }, [searchCustomerId, firstFetch, setFirstFetch, dispatch, user?.id]);

    useEffect(() => {
        if (success) {
            alert('Account created successfully!');
            dispatch(resetState());
        }
    }, [success, dispatch]);

    const handleCreateAccount = () => {
        if (selectedCustomerId && currencyCode) {
            dispatch(createNewAccount({ customerId: selectedCustomerId, createAccountRequest: { currency: currencyCode } }));
        } else {
            alert('Please select a customer and currency.');
        }
    };

    const handleRowClick = (customerId) => {
        setSelectedCustomerId(customerId);
    };

    const handlePageChange = (page) => {
        dispatch(fetchFilteredCustomers({ filterRequest: { userId: user?.id }, page, size: 5, sort: 'name' }));
    };

    const handleSearch = () => {
        dispatch(fetchFilteredCustomers({ filterRequest: { userId: user?.id, customerId: searchCustomerId }, page: 0, size: 5, sort: 'name' }));

    };

    const handleResetSearch = () => {
        setSearchCustomerId('');
        dispatch(fetchFilteredCustomers({ filterRequest: { userId: user?.id }, page: 0, size: 5, sort: 'name' }));
    };

    return (
        <div className="container mt-4">
            <h3>Create Account</h3>
            <div className="row">
                <div className="col-12">
                    {loading && <p>Loading customers...</p>}
                    {error && <p className="text-danger">Error loading customers: {erro.messager}</p>}
                    {createAccountError && <p className="text-danger">Error creating account: {createAccountError.message}</p>}

                    {/* Search Box */}
                    <div className="mb-3">
                        <label htmlFor="searchCustomerId" className="form-label">Search by Customer ID</label>
                        <div className="input-group">
                            <input
                                type="text"
                                id="searchCustomerId"
                                className="form-control"
                                value={searchCustomerId}
                                onChange={(e) => setSearchCustomerId(e.target.value)}
                                placeholder="Enter Customer ID"
                            />
                            <button className="btn btn-primary" onClick={handleSearch}>Search</button>
                            <button className="btn btn-secondary" onClick={handleResetSearch}>Show All</button>
                        </div>
                    </div>

                    {!loading && customers.length > 0 && (
                        <>
                            <div className="table-responsive">
                                <table className="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>Customer ID</th>
                                            <th>Customer Type</th>
                                            <th>Name / Company</th>
                                            <th>TC Kimlik No / VKN</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {customers.map((customer) => (
                                            <tr
                                                key={customer.id}
                                                onClick={() => handleRowClick(customer.id)}
                                                style={{
                                                    cursor: 'pointer',
                                                    backgroundColor: selectedCustomerId === customer.id ? '#f0f8ff' : 'transparent',
                                                    border: selectedCustomerId === customer.id ? '2px solid green' : 'none',
                                                }}
                                            >
                                                <td>{customer.id}</td>
                                                <td>{customer.customerType}</td>
                                                <td>{customer.customerType === 'BIREYSEL' ? `${customer.name} ${customer.surname}` : customer.companyName}</td>
                                                <td>{customer.customerType === 'BIREYSEL' ? customer.tcKimlikNo : customer.vkn}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>

                            <div className="mt-3">
                                <p><strong>Selected Customer ID:</strong> {selectedCustomerId ? selectedCustomerId : 'None'}</p>
                            </div>

                            <div className="d-flex justify-content-between">
                                <button
                                    className="btn btn-secondary"
                                    onClick={() => handlePageChange(currentPage - 1)}
                                    disabled={currentPage <= 0}
                                >
                                    Previous
                                </button>
                                <span>Page {currentPage + 1} of {totalPages}</span>
                                <button
                                    className="btn btn-secondary"
                                    onClick={() => handlePageChange(currentPage + 1)}
                                    disabled={currentPage >= totalPages - 1}
                                >
                                    Next
                                </button>
                            </div>
                        </>
                    )}

                    <div className="mt-3">
                        <label htmlFor="currencyCode" className="form-label">Currency</label>
                        <select
                            id="currencyCode"
                            name="currencyCode"
                            className="form-select"
                            value={currencyCode}
                            onChange={(e) => setCurrencyCode(e.target.value)}
                        >
                            <option key='none' value=''>none</option>
                            {currencies.map((currency) => (
                                <option key={currency.code} value={currency.code}>{currency.code}</option>
                            ))}
                        </select>
                    </div>

                    <button
                        className="btn btn-primary mt-3"
                        onClick={handleCreateAccount}
                        disabled={!selectedCustomerId || !currencyCode}
                    >
                        Create Account
                    </button>
                </div>
            </div>
        </div>
    );
};

export default AddAccountPage;
