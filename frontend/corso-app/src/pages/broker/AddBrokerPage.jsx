
const AddBrokerPage = () => {
    const [broker, setBroker] = useState({
        name: "",
        surname: "",
        email: "",
        phone: "",
    });

    const handleChange = (e) => {
        setBroker({ ...broker, [e.target.name]: e.target.value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(broker);
    };

    return (
        <div>
            <h1>Add Broker</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Name</label>
                    <input
                        type="text"
                        name="name"
                        value={broker.name}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label>Surname</label>
                    <input
                        type="text"
                        name="surname"
                        value={broker.surname}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label>Email</label>
                    <input
                        type="email"
                        name="email"
                        value={broker.email}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label>Phone</label>
                    <input
                        type="text"
                        name="phone"
                        value={broker.phone}
                        onChange={handleChange}
                    />
                </div>
                <button type="submit">Add Broker</button>
            </form>
        </div>
    );
};


export default AddBrokerPage;