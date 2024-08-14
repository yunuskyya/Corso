import { useEffect } from "react";
import { fetchSystemDate } from "../../features/systemDateSlice";
import { useAppDispatch, useAppSelector } from "../../redux/hooks";

const SystemDate = () => {
    const dispatch = useAppDispatch();
    const { systemDate, loading, error } = useAppSelector((state) => state.systemDate);

    useEffect(() => {
        // Fetch the system date immediately
        dispatch(fetchSystemDate());

        // Set up an interval to fetch the system date every 10 seconds
        const intervalId = setInterval(() => {
            dispatch(fetchSystemDate());
        }, 10000); // 10000ms = 10 seconds

        // Clean up the interval on component unmount
        return () => clearInterval(intervalId);
    }, [dispatch]);

    return (
        <div className="row h-100 me-1 fw-bold">
            {loading ? (
                <p>Sistem tarihi...</p>
            ) : error ? (
                <p>Error: {error.message}</p>
            ) : (
                <p className={`text-${systemDate.dayClosedStarted ? 'danger' : 'success'} col align-self-center m-0 p-0`}>Sistem Saati: {new Date(systemDate.date).toLocaleDateString()}</p>
            )}
        </div>
    );
};

export default SystemDate;
