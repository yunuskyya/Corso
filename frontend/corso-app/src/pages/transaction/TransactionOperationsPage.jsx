import React, { useState } from 'react';

const TransactionOperationsPage = () => {
  const [selectedCustomer, setSelectedCustomer] = useState('');
  const [selectedSellCurrency, setSelectedSellCurrency] = useState('');
  const [selectedBuyCurrency, setSelectedBuyCurrency] = useState('');
  const [amount, setAmount] = useState('');
  const [isSellCurrencyDisabled, setSellCurrencyDisabled] = useState(true);
  const [isBuyCurrencyDisabled, setBuyCurrencyDisabled] = useState(true);
  const [isAmountInputDisabled, setAmountInputDisabled] = useState(true);
  const [isConfirmDisabled, setConfirmDisabled] = useState(true);

  const handleCustomerChange = (event) => {
    setSelectedCustomer(event.target.value);
    setSellCurrencyDisabled(false);
  };

  const handleSellCurrencyChange = (event) => {
    setSelectedSellCurrency(event.target.value);
    setBuyCurrencyDisabled(false);
    setAmountInputDisabled(false);
  };

  const handleBuyCurrencyChange = (event) => {
    setSelectedBuyCurrency(event.target.value);
  };

  const handleAmountChange = (event) => {
    setAmount(event.target.value);
    setConfirmDisabled(false);
  };

  return (
    <div className="container mt-5">
      {/* Müşteri Seçimi */}
      <div className="mb-4">
        <label htmlFor="customerSelect" className="form-label">Select Customer</label>
        <select className="form-select" id="customerSelect" value={selectedCustomer} onChange={handleCustomerChange}>
          <option value="" disabled>Select a customer</option>
          {/* Dinamik olarak yüklenen müşteri listesi burada olacak */}
          <option value="CUS001">John Doe</option>
          <option value="CUS002">Jane Smith</option>
        </select>
      </div>

      {/* Satılacak Döviz Seçimi */}
      <div className="mb-4">
        <label htmlFor="sellCurrencySelect" className="form-label">Select Currency to Sell</label>
        <select className="form-select" id="sellCurrencySelect" value={selectedSellCurrency} onChange={handleSellCurrencyChange} disabled={isSellCurrencyDisabled}>
          <option value="" disabled>Select a currency</option>
          {/* Dinamik olarak yüklenen döviz hesapları burada olacak */}
        </select>
      </div>

      {/* Alınacak Döviz Seçimi */}
      <div className="mb-4">
        <label htmlFor="buyCurrencySelect" className="form-label">Select Currency to Buy</label>
        <select className="form-select" id="buyCurrencySelect" value={selectedBuyCurrency} onChange={handleBuyCurrencyChange} disabled={isBuyCurrencyDisabled}>
          <option value="" disabled>Select a currency</option>
          <option value="USD">USD</option>
          <option value="EUR">EUR</option>
          <option value="GBP">GBP</option>
        </select>
      </div>

      {/* Miktar Girişi */}
      <div className="mb-4">
        <label htmlFor="amountInput" className="form-label">Amount</label>
        <input type="number" className="form-control" id="amountInput" placeholder="Enter amount" value={amount} onChange={handleAmountChange} disabled={isAmountInputDisabled} />
      </div>

      {/* Önizleme Alanı */}
      <div className="card mb-4">
        <div className="card-body">
          <h5 className="card-title">Transaction Preview</h5>
          <p className="card-text">Selected Customer: {selectedCustomer ? selectedCustomer : 'None'}</p>
          <p className="card-text">Selling: {selectedSellCurrency ? selectedSellCurrency : 'None'} {amount}</p>
          <p className="card-text">Buying: {selectedBuyCurrency ? selectedBuyCurrency : 'None'} {amount ? (amount * 1.1).toFixed(2) : ''}</p>
          <button className="btn btn-primary" disabled={isConfirmDisabled}>Confirm Transaction</button>
        </div>
      </div>
    </div>
  );
};

export default TransactionOperationsPage;
