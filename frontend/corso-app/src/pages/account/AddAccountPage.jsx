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
            <h3>Hesap Oluştur</h3>
            <div className="row">
                <div className="col-12">
                    {loading && <p>Müşteriler yükleniyor...</p>}
                    {error && <p className="text-danger">Hata: {error.messager}</p>}
                    {createAccountError && <p className="text-danger">Müşteri oluşturulurken hata oluştu: {createAccountError.message}</p>}

                    {/* Search Box */}
                    <div className="mb-3">
                        <label htmlFor="searchCustomerId" className="form-label">Müşteri no ile ara</label>
                        <div className="input-group">
                            <input
                                type="text"
                                id="searchCustomerId"
                                className="form-control"
                                value={searchCustomerId}
                                onChange={(e) => setSearchCustomerId(e.target.value)}
                                placeholder="Müşteri No Giriniz"
                            />
                            <button className="btn btn-primary" onClick={handleSearch}>Ara</button>
                            <button className="btn btn-secondary" onClick={handleResetSearch}>Hepsini Getir</button>
                        </div>
                    </div>

                    {!loading && customers.length > 0 && (
                        <>
                            <div className="table-responsive">
                                <table className="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>Müşteri No</th>
                                            <th>Müşteri Tipi</th>
                                            <th>İsim / Kurum adı</th>
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
                                                <td>{customer.customerType === 'BIREYSEL' ? `${customer.name}` : customer.companyName}</td>
                                                <td>{customer.customerType === 'BIREYSEL' ? customer.tcKimlikNo : customer.vkn}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>

                            <div className="mt-3">
                                <p className='fs-4 text-warning'><strong>Seçilen Müşteri No:</strong> {selectedCustomerId ? selectedCustomerId : 'None'}</p>
                            </div>

                            <div className="d-flex justify-content-between">
                                <button
                                    className="btn btn-secondary"
                                    onClick={() => handlePageChange(currentPage - 1)}
                                    disabled={currentPage <= 0}
                                >
                                    Önceki
                                </button>
                                <span>Sayfa {currentPage + 1} / {totalPages}</span>
                                <button
                                    className="btn btn-secondary"
                                    onClick={() => handlePageChange(currentPage + 1)}
                                    disabled={currentPage >= totalPages - 1}
                                >
                                    Sonraki
                                </button>
                            </div>
                        </>
                    )}

                    <div className="col-4 mt-3">
                        <label htmlFor="currencyCode" className="form-label">Döviz</label>
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
                        Hesap Oluştur
                    </button>
                </div>
            </div>
        </div>
    );
};

export default AddAccountPage;
