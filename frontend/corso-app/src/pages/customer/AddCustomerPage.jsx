import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { createNewCustomer, resetCustomerStatus } from '../../features/customerSlice';

const AddCustomerPage = () => {
    const dispatch = useDispatch();
    const customerState = useSelector(state => state.customer);

    const [formData, setFormData] = useState({
        name: '',
        surname: '',
        customerType: '',
        tcKimlikNo: '',
        companyName: '',
        vkn: '',
        phone: '',
        email: '',
        userId: ''  // Bu alanı gerektiği şekilde doldurun
    });

    const [emailError, setEmailError] = useState('');

    useEffect(() => {
        if (formData.customerType === 'Kurumsal') {
            setFormData(prevData => ({
                ...prevData,
                companyName: '',
                vkn: ''
            }));
        } else {
            setFormData(prevData => ({
                ...prevData,
                companyName: '',
                vkn: ''
            }));
        }
    }, [formData.customerType]);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.id]: e.target.value
        });

        if (e.target.id === 'email') {
            const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailPattern.test(e.target.value)) {
                setEmailError('Geçerli bir e-posta adresi girin.');
            } else {
                setEmailError('');
            }
        }
    };

    const handleAddCustomer = (e) => {
        e.preventDefault();
        if (emailError) {
            alert('Lütfen geçerli bir e-posta adresi girin.');
            return;
        }
        dispatch(createNewCustomer(formData));
    };

    const handleResetForm = () => {
        setFormData({
            name: '',
            surname: '',
            customerType: '',
            tcKimlikNo: '',
            companyName: '',
            vkn: '',
            phone: '',
            email: '',
            userId: ''
        });
        dispatch(resetCustomerStatus());
    };

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-8">
                    <div className="card">
                        <div className="card-header text-center">
                            <h3>Müşteri Ekle</h3>
                        </div>
                        <div className="card-body">
                            <form onSubmit={handleAddCustomer} onReset={handleResetForm}>
                                <div className="row mb-3">
                                    <div className="col-md-6">
                                        <label htmlFor="customerType" className="form-label">Müşteri Tipi</label>
                                        <select id="customerType" className="form-select" value={formData.customerType} onChange={handleChange}>
                                            <option value="" disabled>Seçiniz...</option>
                                            <option value="Kurumsal">Kurumsal</option>
                                            <option value="Bireysel">Bireysel</option>
                                        </select>
                                    </div>
                                    <div className="col-md-6">
                                        <label htmlFor="name" className="form-label">İsim</label>
                                        <input type="text" className="form-control" id="name" placeholder="İsim girin" value={formData.name} onChange={handleChange} />
                                    </div>
                                    <div className="col-md-6">
                                        <label htmlFor="surname" className="form-label">Soyisim</label>
                                        <input type="text" className="form-control" id="surname" placeholder="Soyisim girin" value={formData.surname} onChange={handleChange} />
                                    </div>
                                </div>
                                <div className="row mb-3">
                                    <div className="col-md-6">
                                        <label htmlFor="companyName" className="form-label">Kurum Adı</label>
                                        <input type="text" className="form-control" id="companyName" placeholder="Kurum Adı girin" value={formData.companyName} onChange={handleChange} disabled={formData.customerType !== 'Kurumsal'} />
                                    </div>
                                    <div className="col-md-6">
                                        <label htmlFor="tcKimlikNo" className="form-label">TCKN</label>
                                        <input type="text" className="form-control" id="tcKimlikNo" placeholder="TCKN girin" value={formData.tcKimlikNo} onChange={handleChange} />
                                    </div>
                                </div>
                                <div className="row mb-3">
                                    <div className="col-md-6">
                                        <label htmlFor="vkn" className="form-label">VKN</label>
                                        <input type="text" className="form-control" id="vkn" placeholder="VKN girin" value={formData.vkn} onChange={handleChange} disabled={formData.customerType !== 'Kurumsal'} />
                                    </div>
                                    <div className="col-md-6">
                                        <label htmlFor="email" className="form-label">Email</label>
                                        <input type="email" className="form-control" id="email" placeholder="Email girin" value={formData.email} onChange={handleChange} />
                                        {emailError && <div className="text-danger">{emailError}</div>}
                                    </div>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="phone" className="form-label">GSM</label>
                                    <input type="text" className="form-control" id="phone" placeholder="GSM girin" value={formData.phone} onChange={handleChange} />
                                </div>
                                <button type="submit" className="btn btn-primary w-100 mb-2">Müşteri Ekle</button>
                                <button type="reset" className="btn btn-secondary w-100">Temizle</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AddCustomerPage;
