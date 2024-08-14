import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { createNewCustomer, resetCustomerStatus } from '../../features/customerSlice';
import Modal from '../../components/Common/Modal';
import { GeneralSpinner } from './../../components/Common/GeneralSpinner';

const AddCustomerPage = () => {
    const dispatch = useAppDispatch();
    const customerState = useAppSelector(state => state.customer);
    const [showModal, setShowModal] = useState(false);

    console.log('customerState: ', customerState);

    const [formData, setFormData] = useState({
        name: '',
        surname: '',
        customerType: 'BIREYSEL',
        tcKimlikNo: '',
        companyName: '',
        vkn: '',
        phone: '',
        email: ''
    });

    const [formErrors, setFormErrors] = useState({
        nameError: '',
        surnameError: '',
        tcKimlikNoError: '',
        companyNameError: '',
        vknError: '',
        customerTypeError: '',
        emailError: '',
        gsmError: ''
    });

    useEffect(() => {
        if (formData.customerType === 'KURUMSAL') {
            setFormData(prevData => ({
                ...prevData,
                name: '',
                surname: '',
                tcKimlikNo: ''
            }));
        } else {
            setFormData(prevData => ({
                ...prevData,
                companyName: '',
                vkn: ''
            }));
        }
    }, [formData.customerType]);

    useEffect(() => {
        if (customerState.status === 'succeeded' || customerState.status === 'failed') {
            setShowModal(true);
        }
    }, [customerState.status, setShowModal]);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.id]: e.target.value
        });

        checkErrors(e.target.id, e.target.value, setFormErrors);
    };

    const handleAddCustomer = (e) => {
        e.preventDefault();
        if (Object.values(formErrors).some(err => err !== '')) {
            alert('Lütfen formu doğru şekilde doldurunuz.');
            return;
        }

        dispatch(createNewCustomer(formData));
    };

    const handleResetForm = () => {
        setShowModal(false);
        setFormData(prevData => ({
            ...prevData,
            name: '',
            surname: '',
            customerType: 'BIREYSEL',
            tcKimlikNo: '',
            companyName: '',
            vkn: '',
            phone: '',
            email: ''
        }));
        setFormErrors({
            nameError: '',
            surnameError: '',
            tcKimlikNoError: '',
            companyNameError: '',
            vknError: '',
            customerTypeError: '',
            emailError: '',
            gsmError: ''
        });
        dispatch(resetCustomerStatus());
    };

    function handleHide() {
        setShowModal(false);
    }

    function handleShow() {
        setShowModal(true);
    }

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-8">
                    <div className="card">
                        <div className="card-header text-center">
                            <h3>Müşteri Ekle</h3>
                            {customerState.status === 'succeeded' && <div className='bg-success'>
                                <div>{"Oluşturuldu"} <button className='m-1 p-1 rounded' onClick={handleResetForm}>Tamam</button></div>
                            </div>}
                            {customerState.status === 'failed' && <div className='bg-warning'>
                                <div>{`Hata: ${customerState.error}`} <button className='m-1 p-1 rounded' onClick={handleResetForm}>Kapat</button></div>
                            </div>}
                            {customerState.status === 'loading' && <GeneralSpinner />}
                        </div>
                        <div className="card-body">
                            <form onSubmit={handleAddCustomer} onReset={handleResetForm}>
                                <div className="row mb-3">
                                    <div className="col-md-6">
                                        <label htmlFor="customerType" className="form-label">Müşteri Tipi</label>
                                        <select id="customerType" className="form-select" value={formData.customerType} onChange={handleChange}>
                                            <option value="" disabled>Seçiniz...</option>
                                            <option value="KURUMSAL">Kurumsal</option>
                                            <option value="BIREYSEL">Bireysel</option>
                                        </select>
                                        {formErrors.customerTypeError && <div className="text-danger">{formErrors.customerTypeError}</div>}
                                    </div>
                                    <div className="col-md-6">
                                        <label htmlFor="name" className="form-label">İsim</label>
                                        <input type="text" className="form-control" id="name" placeholder="İsim girin" value={formData.name} onChange={handleChange} disabled={formData.customerType !== 'BIREYSEL'} />
                                        {formErrors.nameError && <div className="text-danger">{formErrors.nameError}</div>}
                                    </div>
                                    <div className="col-md-6">
                                        <label htmlFor="surname" className="form-label">Soyisim</label>
                                        <input type="text" className="form-control" id="surname" placeholder="Soyisim girin" value={formData.surname} onChange={handleChange} disabled={formData.customerType !== 'BIREYSEL'} />
                                        {formErrors.surnameError && <div className="text-danger">{formErrors.surnameError}</div>}
                                    </div>
                                </div>
                                <div className="row mb-3">
                                    <div className="col-md-6">
                                        <label htmlFor="companyName" className="form-label">Kurum Adı</label>
                                        <input type="text" className="form-control" id="companyName" placeholder="Kurum Adı girin" value={formData.companyName} onChange={handleChange} disabled={formData.customerType !== 'KURUMSAL'} />
                                        {formErrors.companyNameError && <div className="text-danger">{formErrors.companyNameError}</div>}
                                    </div>
                                    <div className="col-md-6">
                                        <label htmlFor="tcKimlikNo" className="form-label">TCKN</label>
                                        <input type="text" className="form-control" id="tcKimlikNo" placeholder="TCKN girin" value={formData.tcKimlikNo} onChange={handleChange} disabled={formData.customerType !== 'BIREYSEL'} />
                                        {formErrors.tcKimlikNoError && <div className="text-danger">{formErrors.tcKimlikNoError}</div>}
                                    </div>
                                </div>
                                <div className="row mb-3">
                                    <div className="col-md-6">
                                        <label htmlFor="vkn" className="form-label">VKN</label>
                                        <input type="text" className="form-control" id="vkn" placeholder="VKN girin" value={formData.vkn} onChange={handleChange} disabled={formData.customerType !== 'KURUMSAL'} />
                                        {formErrors.vknError && <div className="text-danger">{formErrors.vknError}</div>}
                                    </div>
                                    <div className="col-md-6">
                                        <label htmlFor="email" className="form-label">Email</label>
                                        <input type="email" className="form-control" id="email" placeholder="Email girin" value={formData.email} onChange={handleChange} />
                                        {formErrors.emailError && <div className="text-danger">{formErrors.emailError}</div>}
                                    </div>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="phone" className="form-label">GSM</label>
                                    <input type="text" className="form-control" id="phone" placeholder="GSM girin" value={formData.phone} onChange={handleChange} />
                                    {formErrors.gsmError && <div className="text-danger">{formErrors.gsmError}</div>}
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

function checkErrors(id, value, setFormErrors) {
    const onlyLettersRegex = /^[a-zA-ZığüşöçİĞÜŞÖÇ]+$/;
    const onlyNumbersRegex = /^[0-9]+$/;

    switch (id) {
        case 'name':
            setFormErrors(prevErrors => ({
                ...prevErrors,
                nameError: !onlyLettersRegex.test(value) ? 'İsim sadece harflerden oluşmalıdır.' :
                    value.length < 2 ? 'İsim en az 2 karakter olmalıdır.' : ''
            }));
            break;
        case 'surname':
            setFormErrors(prevErrors => ({
                ...prevErrors,
                surnameError: !onlyLettersRegex.test(value) ? 'Soyisim sadece harflerden oluşmalıdır.' :
                    value.length < 2 ? 'Soyisim en az 2 karakter olmalıdır.' : ''
            }));
            break;
        case 'tcKimlikNo':
            setFormErrors(prevErrors => ({
                ...prevErrors,
                tcKimlikNoError: value.length !== 11 ? 'TCKN 11 karakter olmalıdır.' :
                    !onlyNumbersRegex.test(value) ? 'TCKN sadece rakamlardan oluşmalıdır.' : ''
            }));
            break;
        case 'companyName':
            setFormErrors(prevErrors => ({
                ...prevErrors,
                companyNameError: !onlyLettersRegex.test(value) ? 'Kurum adı sadece harflerden oluşmalıdır.' :
                    value.length < 2 ? 'Kurum adı en az 2 karakter olmalıdır.' : ''
            }));
            break;
        case 'vkn':
            setFormErrors(prevErrors => ({
                ...prevErrors,
                vknError: value.length !== 10 ? 'VKN 10 karakter olmalıdır.' :
                    !onlyNumbersRegex.test(value) ? 'VKN sadece rakamlardan oluşmalıdır.' : ''
            }));
            break;
        case 'email':
            setFormErrors(prevErrors => ({
                ...prevErrors,
                emailError: !value.includes('@') || !value.includes('.') ? 'Geçerli bir email adresi giriniz.' : ''
            }));
            break;
        case 'phone':
            setFormErrors(prevErrors => ({
                ...prevErrors,
                gsmError: value.length !== 11 ? 'GSM 11 karakter olmalıdır.' :
                    !onlyNumbersRegex.test(value) ? 'GSM sadece rakamlardan oluşmalıdır.' : ''
            }));
            break;
        default:
            break;
    }
}

export default AddCustomerPage;
