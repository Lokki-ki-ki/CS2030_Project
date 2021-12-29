**Level5**

    RandomGenerator(seed, arrivalrate, servicerate, rho)
    Step1 generate the customers with genInterArrival(), whether greedy() -done
    Step2 add serviceTime in DoneEvent -done
    Step3 edit rest in DoneEvent -> If rest, generate the restTime -done
    Step4 deal with greedy

**Simulate**

    1.ArriveEvent:
        -check canServe() 
            (1) idle -> add ServeEvent()
        -check-Self-checkout canserve()
            inside all self checkout, at leaset one "idle" -> ServeEvent()
        -check greedy()
        -check canQueue() -> throw WaitEvent() & add in queueList
            (1) working & empty seat in queue
            (2) rest & has next & empty seat in queue
        -check self-checkout can queue() ->total queue exceed?
            add in totoal queue()
            
    2.WaitEvent()

    3.RestEvent()
        (1)has next -> working ServeEvent(next)
        (2)no next -> idle

    4.ServeEvent()
        serve() -> DoneEvent()
    
    5.DoneEvent
        -new check if rest
        (0) self-checkout has next(total queue has) ->add ServeNext()
        (0) self checkout no next -> "idle"
        (1) no rest & no next -> idle
        (2) no rest & has next -> ServeEvent(next)
        (3) rest  -> status & RestEvent()



**Server**


**Events**

    1.ArriveEvent
    2.WaitEvent
    3.RestStart
    4.ServeEvent
    5.DoneEvent
    6.LeaveEvent
