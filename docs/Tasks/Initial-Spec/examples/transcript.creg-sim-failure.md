# Transcript: CREG und defekte SIM

```text
> AT+CMEE=2
< OK

# Injection: sim.state=SIM_FAILURE

> AT+CPIN?
< +CME ERROR: SIM failure

> AT+CREG?
< +CME ERROR: SIM failure

# Profilvariante: einige Geräte liefern trotz SIM-Fehler Registration-Status
# Erwartung muss daher im Profil als policy definiert werden.
```
