'use strict';

describe('Controller Tests', function() {

    describe('Depense Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDepense, MockOperation, MockFournisseur, MockCompte, MockMode_de_paiment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDepense = jasmine.createSpy('MockDepense');
            MockOperation = jasmine.createSpy('MockOperation');
            MockFournisseur = jasmine.createSpy('MockFournisseur');
            MockCompte = jasmine.createSpy('MockCompte');
            MockMode_de_paiment = jasmine.createSpy('MockMode_de_paiment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Depense': MockDepense,
                'Operation': MockOperation,
                'Fournisseur': MockFournisseur,
                'Compte': MockCompte,
                'Mode_de_paiment': MockMode_de_paiment
            };
            createController = function() {
                $injector.get('$controller')("DepenseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pfeApp:depenseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
