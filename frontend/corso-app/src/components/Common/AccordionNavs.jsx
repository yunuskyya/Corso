import useAuth from '../../hooks/useAuth';
import { Link } from 'react-router-dom';
import { ADMIN_OPERATIONS, BROKER_OPERATIONS, MANAGER_OPERATIONS } from '../../constants/routes';

const AccordionNavs = ({ variant }) => {

    const { user, role } = useAuth();

    console.log("role for navs: ", role);

    const operations = () => {
        switch (role) {
            case 'ROLE_ADMIN': return ADMIN_OPERATIONS;
            case 'ROLE_MANAGER': return MANAGER_OPERATIONS;
            case 'ROLE_BROKER': return BROKER_OPERATIONS;
            default: return null;
        }
    }

    console.log('operations: ', operations());
    return (
        variant === 'sidebar' ? <AccordionSidebar operations={operations()} /> : <AccordionNav operations={operations()} />
    );
};


function AccordionSidebar({ operations }) {
    console.log('operations: ', operations);
    return (
        <li className={`accordion`} id="accordionExample">

            {operations.map((operation, index) => (
                <AccordionItem key={index} operation={operation} bs_parent="#accordionExample" />
            ))}
            {/* <div className="accordion-item">
                <h2 className="accordion-header">
                    <button className="accordion-button fw-bold bg-light" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                        Müşteri İşlemleri
                    </button>
                </h2>
                <div id="collapseOne" className="accordion-collapse collapse show" data-bs-parent="#accordionExample">
                    <div className="accordion-body">
                        <Link className="nav-link fs-6" to="/dashboard/addCustomer">Ekle</Link>
                    </div>
                    <div className="accordion-body nav-item">
                        <Link className="nav-link fs-6" to="/dashboard/listCustomer">Listele</Link>
                    </div>
                    <div className="accordion-body nav-item">
                        <Link className="nav-link fs-6" to="/dashboard/updateCustomer">Güncelle</Link>
                    </div>
                </div>
            </div>
            <div className="accordion-item">
                <h2 className="accordion-header">
                    <button className="accordion-button fw-bold bg-light" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="true" aria-controls="collapseOne">
                        Hesap İşlemleri
                    </button>
                </h2>
                <div id="collapseTwo" className="accordion-collapse collapse show" data-bs-parent="#accordionExample">
                    <div className="accordion-body">
                        <Link className="nav-link fs-6" to="/addCustomer">Hesap Ekle</Link>
                    </div>
                </div>
            </div>
            <Link className="nav-link bg-light fs-6" to="/addCustomer">Hesap Ekle</Link>
            <Link className="nav-link fs-6" to="/addCustomer">Hesap Ekle</Link> */}
        </li>
    );
}

function AccordionNav({ operations }) {
    console.log('ACCORDION NAV operations: ', operations);
    return (
        <li className="nav-item dropdown">
            {operations.map((operation, index) => (
                operation.title_link ? (
                    <Link
                        key={index}
                        className="nav-link ps-3 fw-bold bg-light button"
                        to={operation.title_link}
                        role="button"
                    >
                        {operation.title}
                    </Link>
                ) : (
                    <div key={index}>
                        <a
                            className="nav-link dropdown-toggle"
                            href="#"
                            id={`dropdown${index}`}
                            role="button"
                            data-bs-toggle="dropdown"
                            aria-expanded="false"
                        >
                            {operation.title}
                        </a>
                        <ul className="dropdown-menu" aria-labelledby={`dropdown${index}`}>
                            {operation.operations.map((op, subIndex) => (
                                <li key={subIndex}>
                                    <Link className="dropdown-item" to={op.link}>{op.title}</Link>
                                </li>
                            ))}
                        </ul>
                    </div>
                )
            ))}


            {/* <a className="nav-link dropdown-toggle" href="#" id="themeDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                Müşteri İşlemleri
            </a>
            <ul className="dropdown-menu" aria-labelledby="themeDropdown">

                <li>
                    <Link className="dropdown-item" to='/addCustomer'>Ekle</Link>
                </li>
                <li>
                    <Link className="dropdown-item" to='/listCustomer'>Listele</Link>
                </li>
                <li>
                    <Link className="dropdown-item" to='/updateCustomer'>Güncelle</Link>
                </li>
            </ul>
            <a className="nav-link dropdown-toggle" href="#" id="themeDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                Hesap İşlemleri
            </a>
            <ul className="dropdown-menu" aria-labelledby="themeDropdown">

                <li>
                    <Link className="dropdown-item" to='/addCustomer'>Ekle</Link>
                </li>
                <li>
                    <Link className="dropdown-item" to='/listCustomer'>Listele</Link>
                </li>
                <li>
                    <Link className="dropdown-item" to='/updateCustomer'>Güncelle</Link>
                </li>
            </ul> */}
        </li>
    );
}


function AccordionItem({ operation, bs_parent }) {
    return (
        operation.title_link ? (
            <Link
                className="nav-link ps-3 fw-bold bg-light button"
                to={operation.title_link}
                role="button"
            >
                {operation.title}
            </Link>
        ) : (
            <div className="accordion-item">
                <h2 className="accordion-header">
                    <button
                        className="accordion-button fw-bold bg-light"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target={`#${operation.title.replace(' ', '')}`}
                        aria-expanded="true"
                        aria-controls={operation.title}
                    >
                        {operation.title}
                    </button>
                </h2>
                <div
                    id={operation.title.replace(' ', '')}
                    className="accordion-collapse collapse show"
                    data-bs-parent={bs_parent}
                >
                    {operation.operations.map((op, index) => (
                        <div key={index} className="accordion-body">
                            <Link
                                className="nav-link ps-3"
                                to={op.link}
                                role="button"
                            >
                                {op.title}
                            </Link>
                        </div>
                    ))}
                </div>
            </div>
        )
    );
}

export default AccordionNavs;