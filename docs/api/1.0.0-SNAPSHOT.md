# API Docs - v1.0.0-SNAPSHOT

## Sink

### hl7 *<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#sink">(Sink)</a>*

<p style="word-wrap: break-word">The hl7 pushes the hl7 message into HAPI client using MLLP protocol </p>

<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>
```
@sink(type="hl7", host.name="<STRING>", port="<INT>", hl7.encoding="<STRING>", ack.hl7.encoding="<STRING>", charset="<STRING>", tls.enabled="<BOOL>", hl7.timeout="<INT>", @map(...)))
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">host.name</td>
        <td style="vertical-align: top; word-wrap: break-word">The target host where the HL7 message will be pushed</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">port</td>
        <td style="vertical-align: top; word-wrap: break-word">This is the unique logical address used to establish the connection for the process.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">hl7.encoding</td>
        <td style="vertical-align: top; word-wrap: break-word">Encoding method of hl7</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">ack.hl7.encoding</td>
        <td style="vertical-align: top; word-wrap: break-word">Encoding method of hl7 to log the acknowledgment message</td>
        <td style="vertical-align: top">ER7</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">charset</td>
        <td style="vertical-align: top; word-wrap: break-word">Character encoding method</td>
        <td style="vertical-align: top">UTF-8</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">tls.enabled</td>
        <td style="vertical-align: top; word-wrap: break-word">Transport layer security</td>
        <td style="vertical-align: top">false</td>
        <td style="vertical-align: top">BOOL</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">hl7.timeout</td>
        <td style="vertical-align: top; word-wrap: break-word">Sets the time that the initiator will wait for a response for a given message before timing out and throwing an exception</td>
        <td style="vertical-align: top">10000</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
@App:name('TestExecutionPlanSink') 
@sink(type = 'hl7',
host.name = 'localhost',
port=1080,
hl7.encoding = 'ER7',
@map(type = 'text'))
define stream hl7stream(payload string);

```
<p style="word-wrap: break-word">This publishes the HL7 messages using the MLLP protocol and receive and log the acknowledgement message.<br>&nbsp;</p>

## Source

### hl7 *<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#source">(Source)</a>*

<p style="word-wrap: break-word">The hl7 consumes the hl7 message using MLLP protocol </p>

<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>
```
@source(type="hl7", port="<INT>", tls.enabled="<BOOL>", charset="<STRING>", rechl7.encoding="<STRING>", hl7.timeout="<INT>", @map(...)))
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">port</td>
        <td style="vertical-align: top; word-wrap: break-word">This is the unique logical address used to establish the connection for the process.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">tls.enabled</td>
        <td style="vertical-align: top; word-wrap: break-word">Transport layer security</td>
        <td style="vertical-align: top">false</td>
        <td style="vertical-align: top">BOOL</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">charset</td>
        <td style="vertical-align: top; word-wrap: break-word">Character encoding method</td>
        <td style="vertical-align: top">UTF-8</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">rechl7.encoding</td>
        <td style="vertical-align: top; word-wrap: break-word">Encoding method of received hl7</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">hl7.timeout</td>
        <td style="vertical-align: top; word-wrap: break-word">how long the hl7 should wait for the thread to terminate.</td>
        <td style="vertical-align: top">6000</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
@App:name ( 'TestExecutionPlanSource' ) 
@source ( type = 'hl7',
port = 1080,
recHl7.encoding = 'ER7',
@map (type = 'text'))
define stream hl7stream (payload string);

```
<p style="word-wrap: break-word">This receives the HL7 messages using the MLLP protocol and send the acknowledgement message to the client.<br>&nbsp;</p>

