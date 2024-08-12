import React, { useState, useEffect } from 'react';
import useAuth from '../../hooks/useAuth';
import { fetchCustomerList } from '../../api/transactionApi';
//import { fetchTransactionList } from '../../api/transactionApi';

const ListCashFlowPage = () => {
    const { user } = useAuth();
    const [selectedCustomer, setSelectedCustomer] = useState('');
    const [customerList, setCustomerList] = useState([]);
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [transactionDirection, setTransactionDirection] = useState('');
    const [transactionList, setTransactionList] = useState([]);

    useEffect(() => {
        const getCustomerList = async () => {
            try {
                const response = await fetchCustomerList(user.id);
                setCustomerList(response.content);  // API'den gelen müşteri listesini set ediyoruz
            } catch (error) {
                console.error('Error fetching customer list:', error);
            }
        };
        getCustomerList();
    }, [user.id]);

    const handleListClick = async () => {
        try {
            /*const response = await fetchTransactionList({
                customer: selectedCustomer,
                startDate,
                endDate,
                direction: transactionDirection,
            });
            setTransactionList(response); */
        } catch (error) {
            console.error('Error fetching transaction list:', error);
        }
    };

    const handleClearClick = () => {
        setSelectedCustomer('');
        setStartDate('');
        setEndDate('');
        setTransactionDirection('');
        setTransactionList([]);
    };

    return (
        <div className="container my-4">
            <h2 className="text-center mb-4">Nakit Akışı Listeleme</h2>
            <form>
                {/* Müşteri Seçimi */}
                <div className="mb-3">
                    <label htmlFor="customerSelect" className="form-label">Customer</label>
                    <select
                        id="customerSelect"
                        value={selectedCustomer}
                        onChange={(e) => setSelectedCustomer(e.target.value)}
                        className="form-select"
                        required
                    >
                        <option value="">Müşteri Seçiniz</option>
                        {customerList?.map((customer) => (
                            <option key={customer.id} value={customer.id}>
                                {customer.name} {customer.surname}
                            </option>
                        ))}
                    </select>
                </div>

                {/* Tarih Aralığı Seçimi */}
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="startDate" className="form-label">Start Date</label>
                        <input
                            type="date"
                            id="startDate"
                            value={startDate}
                            onChange={(e) => setStartDate(e.target.value)}
                            className="form-control"
                        />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="endDate" className="form-label">End Date</label>
                        <input
                            type="date"
                            id="endDate"
                            value={endDate}
                            onChange={(e) => setEndDate(e.target.value)}
                            className="form-control"
                        />
                    </div>
                </div>

                {/* İşlem Yönü Seçimi */}
                <div className="mb-3">
                    <label htmlFor="transactionDirection" className="form-label">Transaction Direction</label>
                    <select
                        id="transactionDirection"
                        value={transactionDirection}
                        onChange={(e) => setTransactionDirection(e.target.value)}
                        className="form-select"
                    >
                        <option value="">Nakit Yönünü Seçiniz</option>
                        <option value="IN">Gelen Nakit</option>
                        <option value="OUT">Giden Nakit</option>
                    </select>
                </div>

                {/* Listeleme ve Temizleme Butonları */}
                <div className="d-flex justify-content-center gap-3 mb-4">
                    <button
                        type="button"
                        onClick={handleListClick}
                        className="btn btn-primary btn-lg"
                    >
                        Listele
                    </button>
                    <button
                        type="button"
                        onClick={handleClearClick}
                        className="btn btn-secondary btn-lg"
                    >
                        Temizle
                    </button>
                </div>
            </form>

            {/* İşlem Listesi */}
            <div>
                <h3 className="mb-3">Nakit Akışı Listesi</h3>
                <table className="table table-bordered">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Customer</th>
                            <th>Currency</th>
                            <th>Amount</th>
                            <th>Date</th>
                            <th>Direction</th>
                        </tr>
                    </thead>
                    <tbody>
                        {transactionList.map((transaction) => (
                            <tr key={transaction.id}>
                                <td>{transaction.id}</td>
                                <td>{transaction.customerName}</td>
                                <td>{transaction.currencyType}</td>
                                <td>{transaction.amount}</td>
                                <td>{transaction.date}</td>
                                <td>{transaction.direction}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default ListCashFlowPage;
