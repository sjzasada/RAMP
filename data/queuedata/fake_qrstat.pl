#!/usr/bin/perl
#

use Sys::Hostname;

$myname=hostname;

$timefile="/tmp/".$myname.".timefile";
$logname="/home/stefan/.workspace/RAMP/data/queuedata/ANL-Intrepid-2009-1.swf";

$now=time;


$restime=$ARGV[0];
$resprocs=$ARGV[1];

if (-e $timefile) {

open FILE, "<$timefile" or die $!;

my @lines = <FILE>;

$start=$lines[0];
close FILE;

} else {
open FILE, ">$timefile" or die $!; 
print FILE $now; 
close FILE;
$start=$now;
}

$deltatime=$now-$start;

$procs;

open FILE2, "<$logname" or die $!;

$usedprocs=0;

while (my $line = <FILE2>) {

  if ($line =~ /^\s*$|^;/) {


      if ($line =~ /^;\s*MaxProcs:\s*(\d+)$/) {
            $procs = $1;
        }

	next;
} else {

    $line =~ /^\s*(.*)\s*$/;
    my $data = $1;


    my ($job, $sub, $wait, $t, $p, $cpu, $mem, $preq, $treq, $mreq,
	$status, $u, $gr, $app, $q, $part, $prec, $think) = split(/\s+/,$data);

	$endtime = $sub+$wait+$t;


	#print "sub=".$sub." end=".$endtime." delta=".$deltatime."\n";

    #submitted now
	if ($sub < $deltatime) {

#not already ended
	if ($endtime > $deltatime) {

		if ($sub+$treq > $deltatime+$restime) {
		$usedprocs=$usedprocs+$p;
}
		}
	} else {
		last;
	}

}

}

print $usedprocs."\n";
$availableprocs=$procs-$usedprocs;

if ($resprocs <= $availableprocs) {
    print "Reservation: ".$now."\n";
} else {
    print "failed\n";
}


close FILE2;
