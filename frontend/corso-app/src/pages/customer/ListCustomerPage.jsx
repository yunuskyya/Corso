// src/pages/CustomerListPage.js
import { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { fetchFilteredCustomers, softDeleteCustomerById, resetDeleteState } from '../../features/customerListSlice';
import useAuth from '../../hooks/useAuth';
import moment from 'moment';
import { currencies } from '../../constants/currencies';
import { GeneralSpinner } from '../../components/Common/GeneralSpinner';
import { useNavigate } from 'react-router-dom';

const ListCustomerPage = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const { customers, totalPages, currentPage, loading, error, deleteError, deleteSuccess } = useAppSelector((state) => state.customerList);

    const { user } = useAuth();

    const [filterRequest, setFilterRequest] = useState({
        userId: user ? user.role === 'ROLE_MANAGER' ? null : user.id : null,
        accountId: '',
        customerId: '',
        name: '',
        surname: '',
        companyName: '',
        tcKimlikNo: '',
        vkn: '',
        customerType: 'BIREYSEL', // Default type
        status: '',
        currencyCode: '',
        phone: '',
        email: '',
        dateStart: '',
        dateEnd: '',
    });

    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [sort, setSort] = useState('id,asc');
    const [delayTimer, setDelayTimer] = useState(null);

    const debounceFilter = (callback, delay) => {
        if (delayTimer) {
            clearTimeout(delayTimer);
        }
        const newTimer = setTimeout(callback, delay);
        setDelayTimer(newTimer);
    };

    useEffect(() => {
        debounceFilter(() => {
            dispatch(fetchFilteredCustomers({ filterRequest, page, size, sort }));
        }, 300);
    }, [filterRequest, page, size, sort]);

    const handleFilterChange = (e) => {
        const { name, value } = e.target;
        setFilterRequest((prev) => ({
            ...prev,
            [name]: value,
        }));

        if (name === 'dateStart' || name === 'dateEnd') {
            console.log(name + " " + value)
        }
    };

    const handleCustomerTypeChange = (e) => {
        const customerType = e.target.value;
        setFilterRequest((prev) => ({
            ...prev,
            customerType,
            name: '',
            surname: '',
            vkn: '',
        }));
    };

    const handlePageChange = (newPage) => {
        setPage(newPage);
    };

    const handleSizeChange = (e) => {
        setSize(e.target.value);
    };

    // const handleSoftDelete = (customer) => {
    //     console.log('Soft delete customer:', customer);
    //     const id = customer.id;

    //     // Creating the updated customer object with only the specified fields and status updated
    //     const updatedCustomer = {
    //         customerType: customer.customerType,
    //         status: 'DELETED',
    //         phone: customer.phone,
    //         email: customer.email
    //     };

    //     // Conditionally adding fields based on customerType
    //     if (customer.customerType === 'KURUMSAL') {
    //         updatedCustomer.companyName = customer.companyName;
    //         updatedCustomer.vkn = customer.vkn;
    //     } else if (customer.customerType === 'BIREYSEL') {
    //         updatedCustomer.name = customer.name;
    //         updatedCustomer.surname = customer.surname;
    //         updatedCustomer.tcKimlikNo = customer.tcKimlikNo;
    //     }

    //     console.log('Updated customer:', updatedCustomer);

    //     dispatch(softDeleteCustomerById({ customerId: id, customer: updatedCustomer }));
    // };




    // const handleCloseAlert = () => {
    //     dispatch(resetDeleteState());
    // };

    return (
        <div className="container my-4">
            <h1 className="mb-4">Müşteri İşlemleri</h1>

            {/* {deleteSuccess && <div className='bg-success'>
                <div>{"Müşteri başarılı bir şekilde silindi"} <button className='m-1 p-1 rounded' onClick={handleCloseAlert}>Tamam</button></div>
            </div>}
            {deleteError && <div className='bg-danger'>
                <div>{"Müşteri silme hatalı: " + deleteError.message} <button className='m-1 p-1 rounded' onClick={handleCloseAlert}>Tamam</button></div>
            </div>} */}

            <div className="mb-4">
                {/* Customer Type Selection */}
                <div className="mb-3">
                    <label htmlFor="customerType" className="form-label">Müşteri Tipi</label>
                    <select
                        id="customerType"
                        name="customerType"
                        className="form-select"
                        value={filterRequest.customerType}
                        onChange={handleCustomerTypeChange}
                    >
                        <option value="BIREYSEL">BIREYSEL</option>
                        <option value="KURUMSAL">KURUMSAL</option>
                    </select>
                </div>

                {/* Individual Customer Filters */}
                {filterRequest.customerType === 'BIREYSEL' && (
                    <div className="row mb-3">
                        <div className="col-6 col-lg-4">
                            <label htmlFor="name" className="form-label">İsim</label>
                            <input
                                type="text"
                                id="name"
                                name="name"
                                className="form-control"
                                value={filterRequest.name}
                                placeholder="isim"
                                onChange={handleFilterChange}
                            />
                        </div>
                        <div className="col-6 col-lg-4">
                            <label htmlFor="surname" className="form-label">Soyisim</label>
                            <input
                                type="text"
                                id="surname"
                                name="surname"
                                className="form-control"
                                value={filterRequest.surname}
                                placeholder="soyisim"
                                onChange={handleFilterChange}
                            />
                        </div>
                        <div className="col-6 col-lg-4">
                            <label htmlFor="tcKimlikNome" className="form-label">TC kimlik</label>
                            <input
                                type="text"
                                id="tcKimlikNo"
                                name="tcKimlikNo"
                                className="form-control"
                                value={filterRequest.tcKimlikNo}
                                placeholder="TC Kimlik No"
                                onChange={handleFilterChange}
                            />
                        </div>
                    </div>
                )}

                {/* Corporate Customer Filters */}
                {filterRequest.customerType === 'KURUMSAL' && (
                    <div className="row mb-3">
                        <div className="col-6">
                            <label htmlFor="companyName" className="form-label">Şirket Adı</label>
                            <input
                                type="text"
                                id="companyName"
                                name="companyName"
                                className="form-control"
                                value={filterRequest.companyName}
                                placeholder="Kurum adı"
                                onChange={handleFilterChange}
                            />
                        </div>
                        <div className="col-6">
                            <label htmlFor="vkn" className="form-label">VKN</label>
                            <input
                                type="text"
                                id="vkn"
                                name="vkn"
                                className="form-control"
                                value={filterRequest.vkn}
                                placeholder="VKN"
                                onChange={handleFilterChange}
                            />
                        </div>
                    </div>
                )}

                {/* Common Filters */}

                <div className="row">
                    {/* <div className="col-6 col-lg-3 mb-3">
                        <label htmlFor="accountId" className="form-label">Hesap NO</label>
                        <input
                            type="text"
                            id="accountId"
                            name="accountId"
                            className="form-control"
                            value={filterRequest.accountId}
                            placeholder="Hesap ID"
                            onChange={handleFilterChange}
                        />
                    </div> */}
                    <div className="col-6 col-lg-3 mb-3">
                        <label htmlFor="customerId" className="form-label">Müşteri No</label>
                        <input
                            type="text"
                            id="customerId"
                            name="customerId"
                            className="form-control"
                            value={filterRequest.customerId}
                            placeholder="Müşteri No"
                            onChange={handleFilterChange}
                        />
                    </div>
                    <div className="col-6 col-lg-3 mb-3">
                        <label htmlFor="currencyCode" className="form-label">Döviz</label>
                        <select
                            id="currencyCode"
                            name="currencyCode"
                            className="form-select"
                            value={filterRequest.currencyCode}
                            onChange={handleFilterChange}
                        >
                            <option key='none' value=''>none</option>
                            {currencies.map((currency) => (
                                <option key={currency.code} value={currency.code}>{currency.code}</option>
                            ))}
                        </select>
                    </div>
                    <div className="col-6 col-lg-3 mb-3">
                        <label htmlFor="phone" className="form-label">Telefon Numarası</label>
                        <input
                            type="text"
                            id="phone"
                            name="phone"
                            className="form-control"
                            value={filterRequest.phone}
                            placeholder="Telefon Numarası"
                            onChange={handleFilterChange}
                        />
                    </div>
                    <div className="col-6 col-lg-3 mb-3">
                        <label htmlFor="email" className="form-label">E-Posta</label>
                        <input
                            type="text"
                            id="email"
                            name="email"
                            className="form-control"
                            value={filterRequest.email}
                            placeholder="E-Posta"
                            onChange={handleFilterChange}
                        />
                    </div>
                    <div className="col-6 col-lg-3 mb-3">
                        <label htmlFor="dateStart" className="form-label">Başlangıç Tarihi</label>
                        <input
                            type="date"
                            id="dateStart"
                            name="dateStart"
                            className="form-control"
                            value={filterRequest.dateStart}
                            onChange={handleFilterChange}
                        />
                    </div>

                    <div className="col-6 col-lg-3 mb-3">
                        <label htmlFor="dateEnd" className="form-label">Bitiş Tarihi</label>
                        <input
                            type="date"
                            id="dateEnd"
                            name="dateEnd"
                            className="form-control"
                            value={filterRequest.dateEnd}
                            onChange={handleFilterChange}
                        />
                    </div>
                </div>


            </div>

            <div className="d-flex justify-content-between mb-4">
                <div>
                    <label htmlFor="pageSize" className="form-label me-2">Max Satır:</label>
                    <select id="pageSize" className="form-select d-inline w-auto" value={size} onChange={handleSizeChange}>
                        <option value="5">5</option>
                        <option value="10">10</option>
                        <option value="20">20</option>
                    </select>
                </div>
                <div className='row'>
                    <div className='col-6'><button
                        className="btn btn-primary p-1 me-1"
                        onClick={() => handlePageChange(page - 1)}
                        disabled={page === 0}
                    >
                        Previous
                    </button>
                    </div>
                    <div className='col-6'>
                        <button
                            className="btn btn-primary p-1"
                            onClick={() => handlePageChange(page + 1)}
                            disabled={page + 1 >= totalPages}
                        >
                            Next
                        </button>
                    </div>

                </div>
            </div>

            {loading && <div className=""><GeneralSpinner /></div>}
            {error && <div className="alert alert-danger">Error: {error.message}</div>}

            {customers.length > 0 ? (
                <div className='table-responsive'><table className="table table-striped table-bordered">
                    <thead>
                        <tr>
                            <th>Müşteri No</th>
                            <th>İsim</th>
                            {/* <th>Hesap ID'leri</th> */}
                            <th>TC Kimlik No</th>
                            <th>VKN</th>
                            <th>Müşteri Tipi</th>
                            <th>Durum</th>
                            <th>Telefon</th>
                            <th>E-Posta</th>
                            <th>Oluşturuldu</th>
                            {user?.role === 'ROLE_BROKER' && <th>İşlemler</th>}
                        </tr>
                    </thead>
                    <tbody>
                        {customers.map((customer) => (
                            <tr key={customer.id}>
                                <td>{customer.id}</td>
                                <td>{customer.name}</td>
                                {/* <td>
                                    <div className="dropdown">
                                        <button className="btn btn-secondary dropdown-toggle" type="button" id={`dropdownMenuButton-${customer.id}`} data-bs-toggle="dropdown" aria-expanded="false">
                                            Accounts
                                        </button>
                                        <ul className="dropdown-menu" aria-labelledby={`dropdownMenuButton-${customer.id}`}>
                                            {customer.accounts.map((a) => (
                                                <li key={a.id}>
                                                    <a className="dropdown-item" href="#">
                                                        {`${a.id} - ${a.currency}`}
                                                    </a>
                                                </li>
                                            ))}
                                        </ul>
                                    </div>
                                </td> */}
                                <td>{customer.tcKimlikNo}</td>
                                <td>{customer.vkn}</td>
                                <td>{customer.customerType}</td>
                                <td>{customer.status}</td>
                                <td>{customer.phone}</td>
                                <td>{customer.email}</td>
                                <td>{formatDate(customer.createdAt)}</td>
                                {user?.role === 'ROLE_BROKER' && (
                                    <td>
                                        {/* <button className="btn btn-danger btn-sm" onClick={() => handleSoftDelete(customer)}>
                                            Sil
                                        </button> */}
                                        <button className="btn btn-warning btn-sm" onClick={() => navigate(`/dashboard/broker/add-account?customerId=${customer.id}`)}>
                                            Hesap Ekle
                                        </button>
                                    </td>
                                )}
                            </tr>
                        ))}
                    </tbody>
                </table>
                </div>

            ) : (
                <p>No customers found.</p>
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


export default ListCustomerPage;
