import React, { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import useAuth from '../../hooks/useAuth';
// import { createNewIban, deleteIban } from '../../features/ibanSlice';
// import { fetchCustomerListForIban } from '../../api/ibanApi';
// import { fetchCustomerList } from '../../api/transactionApi';
// import { fetchIbanListByCustomer } from '../../api/moneyTransferApi';


const IbanPage = () => {
  // const { user } = useAuth();
  // const [customerId, setCustomerId] = useState('');
  // const [iban, setIban] = useState('TR ');
  // const [nickname, setNickname] = useState('');
  // const [currency, setCurrency] = useState('');
  // const [removeIbanValue, setRemoveIbanValue] = useState('');
  // const [ibanError, setIbanError] = useState('');
  // const dispatch = useDispatch();
  // const [ibanList, setIbanList] = useState([]);
  // const [customersv, setCustomers] = useState([]);

  // const MAX_IBAN_LENGTH = 27; // TR + boşluk dahil en fazla 27 karakter

  // // Müşteri listesini API'den çekme işlemi
  // useEffect(() => {
  //   const getCustomerList = async () => {
  //     try {
  //       const response = await fetchCustomerList(user.id); // user.id'yi ilgili kullanıcı id'siyle değiştirin
  //       setCustomers(response.content); // API'den gelen müşteri listesini state'e set edin
  //     } catch (error) {
  //       console.error('Error fetching customer list:', error);
  //     }
  //   };
  //   getCustomerList();
  // }, []);





  // const handleAddIban = (e) => {
  //   e.preventDefault();
  //   if (iban.length !== MAX_IBAN_LENGTH) {
  //     setIbanError('IBAN numarası 24 karakter olmalıdır.');
  //     return;
  //   }
  //   if (iban && currency) {
  //     dispatch(createNewIban({ customerId, iban, nickname, currency }));
  //     handleClearForm();
  //   }
  // };


  // const handleRemoveIban = (e) => {
  //   e.preventDefault();
  //   if (removeIbanValue) {
  //     dispatch(deleteIban({ customerId, iban: removeIbanValue }));
  //     handleClearForm();
  //   }
  // };

  // const handleClearForm = () => {
  //   setCustomerId(''); // Müşteri seçim alanını sıfırlama
  //   setIban('TR ');
  //   setNickname('');
  //   setCurrency('');
  //   setRemoveIbanValue('');
  //   setIbanError('');
  // };

  // const isFormValid = () => {
  //   return (iban && currency) || removeIbanValue;
  // };

  // const handleIbanChange = (e) => {
  //   const value = e.target.value.toUpperCase();

  //   if (value.length > 3 && /[^0-9\s]/.test(value.slice(3))) {
  //     setIbanError('IBAN numarası sadece rakamlardan oluşmalıdır.');
  //     return;
  //   } else {
  //     setIbanError('');
  //   }

  //   if (value.startsWith('TR ') && value.length <= MAX_IBAN_LENGTH) {
  //     setIban(value);
  //   }

  //   if (value.length === MAX_IBAN_LENGTH) {
  //     setIbanError('');
  //   } else if (value.length < MAX_IBAN_LENGTH) {
  //     setIbanError('IBAN numarası 24 karakter olmalıdır.');
  //   }

  //   if (value) {
  //     setRemoveIbanValue('');
  //   }
  // };

  return (
    <div className="container mt-5">
      <h1>IBAN</h1>
      {/* <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-header text-center bg-primary text-white">
              <h3>IBAN İşlemleri</h3>
            </div>
            <div className="card-body">
              <form>
                <div className="mb-3">
                  <label htmlFor="customer" className="form-label">Müşteri</label>
                  <select
                    className="form-select"
                    id="customer"
                    value={customerId}
                    onChange={(e) => setCustomerId(e.target.value)}
                  >
                    <option value="">Müşteri Seçin...</option>
                    {customersv.map((customer) => (
                      <option key={customer.id} value={customer.id}>
                        {customer.name + ' ' + customer.surname}
                      </option>
                    ))}
                  </select>
                </div>
                {customerId && (
                  <>
                    <div className="mb-3">
                      <label htmlFor="addIban" className="form-label">Eklenecek IBAN</label>
                      <input
                        type="text"
                        className="form-control"
                        id="addIban"
                        placeholder="Eklenecek IBAN girin"
                        value={iban}
                        onChange={handleIbanChange}
                        disabled={!!removeIbanValue}
                      />
                      {ibanError && <div className="text-danger mt-2">{ibanError}</div>}
                    </div>
                    <div className="mb-3">
                      <label htmlFor="removeIban" className="form-label">Silinecek IBAN</label>
                      <input
                        type="text"
                        className="form-control"
                        id="removeIban"
                        placeholder="Silinecek IBAN girin"
                        value={removeIbanValue}
                        onChange={(e) => {
                          setRemoveIbanValue(e.target.value);
                          if (e.target.value) {
                            setIban('TR ');
                            setNickname('');
                            setCurrency('');
                          }
                        }}
                        disabled={!!iban && iban !== 'TR '}
                      />
                    </div>
                    {iban && iban !== 'TR ' && (
                      <>
                        <div className="mb-3">
                          <label htmlFor="currency" className="form-label">Döviz Tipi</label>
                          <select
                            className="form-select"
                            id="currency"
                            value={currency}
                            onChange={(e) => setCurrency(e.target.value)}
                          >
                            <option value="">Seçin...</option>
                            <option value="USD">Dolar</option>
                            <option value="TRY">TL</option>
                            <option value="EUR">Euro</option>
                          </select>
                        </div>
                        <div className="mb-3">
                          <label htmlFor="nickname" className="form-label">Kayıt Adı</label>
                          <input
                            type="text"
                            className="form-control"
                            id="nickname"
                            placeholder="Kayıt Adı girin (Opsiyonel)"
                            value={nickname}
                            onChange={(e) => setNickname(e.target.value)}
                          />
                        </div>
                      </>
                    )}
                  </>
                )}
                <div className="row">
                  <div className="col-md-6">
                    <button
                      type="submit"
                      className="btn btn-primary w-100 mb-2"
                      onClick={iban && iban !== 'TR ' ? handleAddIban : handleRemoveIban}
                      disabled={!isFormValid()}
                    >
                      İşlemi Tamamla
                    </button>
                  </div>
                  <div className="col-md-6">
                    <button
                      type="button"
                      className="btn btn-secondary w-100"
                      onClick={handleClearForm}
                    >
                      Temizle
                    </button>
                  </div>
                </div>

              </form>
            </div>
          </div>
        </div>
      </div> */}
    </div>
  );
};

export default IbanPage;